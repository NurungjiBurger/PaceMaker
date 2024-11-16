package com.maker.pacemaker.ui.viewmodel.interview.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.ClovaTTSClient
import com.maker.pacemaker.data.model.remote.ClovaVoiceService
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.media.MediaPlayer
import android.media.MediaRecorder
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.Session
import com.google.gson.JsonParser
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.remote.CV
import com.maker.pacemaker.data.model.remote.ClovaSTTClient
import com.maker.pacemaker.data.model.remote.Interview
import com.maker.pacemaker.data.model.remote.InterviewAnswerRequest
import kotlinx.coroutines.CompletableDeferred
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@HiltViewModel
open class InterviewingScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel,
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val interviewViewModel = base

    val context = baseViewModel.context

    val repository = baseViewModel.repository

    // 질문 리스트 (AI 생성 질문)
    private val _interviews = MutableStateFlow(listOf<Interview>())
    val interviews = _interviews

    // 현재 질문 인덱스
    private val _index = MutableStateFlow(0)
    val index = _index

    // 재답변 기회 (초기값 1번)
    private val _reAnswerCnt = MutableStateFlow(1)
    val reAnswerCnt = _reAnswerCnt

    // 면접관과 면접자의 턴 (false: 면접관, true: 면접자)
    private val _turn = MutableStateFlow(false)
    val turn = _turn

    // 타이머
    private val _timer = MutableStateFlow(-1)
    val timer = _timer

    // 타이머 on off
    private val _timerActive = MutableStateFlow(false)
    val timerActive = _timerActive

    // STT인지 TTS인지 구별하는 변수
    private val _state = MutableStateFlow("")
    val state = _state

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    init {
        // AI 질문 생성 요청 (예시)
        fetchQuestionsFromAI()
    }

    fun restate() {
        _index.value = 0
        _reAnswerCnt.value = 1
        _turn.value = false
        _timer.value = 100
        _timerActive.value = false
        fetchQuestionsFromAI()
    }

    private fun fetchQuestionsFromAI() {
        viewModelScope.launch {
            interviewViewModel.setLoading(true)

            try {
                // 1. 자기소개서 전송
                val sanitizedText = sanitizeText(interviewViewModel.text.value)
                val cvId = sendCVAndGetId(sanitizedText)

                interviewViewModel.setCVId(cvId)

                // 2. CV 준비 상태 확인
                val isReady = waitForCVReady(cvId)
                if (!isReady) {
                    Log.e("InterviewingScreenViewModel", "CV is not ready after retries.")
                    interviewViewModel.setLoading(false)
                    return@launch
                }

                // 3. 인터뷰 질문 데이터 가져오기
                val interviewResponse = fetchInterviews(cvId)

                // 4. 인터뷰 시작
                _interviews.value = interviewResponse
                Log.d("InterviewingScreenViewModel", "Interviews received: $interviewResponse")

                startInterview()
            } catch (e: Exception) {
                Log.e("InterviewingScreenViewModel", "Error in fetchQuestionsFromAI: ${e.message}")
            } finally {
                interviewViewModel.setLoading(false)
            }
        }
    }

    private suspend fun sendCVAndGetId(sanitizedText: String): Int {
        return try {
            val request = CV(sanitizedText)
            val response = repository.sendCV(request)
            Log.d("InterviewingScreenViewModel", "CV sent successfully: $response")
            response.cv_id
        } catch (e: Exception) {
            Log.e("InterviewingScreenViewModel", "Error sending CV: ${e.message}")
            throw e
        }
    }

    // CV 준비 상태를 주기적으로 확인하는 함수
    private suspend fun waitForCVReady(cvId: Int): Boolean {
        repeat(10) { attempt ->
            try {
                val isReady = repository.checkCVReady(cvId)
                Log.d("InterviewingScreenViewModel", "Attempt $attempt: CV ready status = $isReady")
                if (isReady) return true
            } catch (e: Exception) {
                Log.e("InterviewingScreenViewModel", "Error checking CV ready: ${e.message}")
            }
            delay(2000L) // 2초 대기
        }
        return false
    }

    private suspend fun fetchInterviews(cvId: Int): List<Interview> {
        return try {
            val interviews = repository.getInterviewsByCVId(cvId)
            Log.d("InterviewingScreenViewModel", "Interviews fetched successfully: $interviews")

            // answer 필드가 null인 경우 빈 문자열로 대체
            interviews.map { interview ->
                interview.copy(answer = interview.answer ?: "")
            }
        } catch (e: Exception) {
            Log.e("InterviewingScreenViewModel", "Error fetching interviews: ${e.message}")
            throw e
        }
    }

    // 특수문자와 줄바꿈을 처리하는 함수
    private fun sanitizeText(text: String): String {
        return text
            .replace("\\", "\\\\") // 백슬래시 이스케이프
            .replace("\"", "\\\"") // 큰따옴표 이스케이프
            .replace("\n", "\\n") // 줄바꿈 이스케이프
            .replace("\r", "\\r") // 캐리지 리턴 이스케이프
    }

    private fun initialisze() {
        _reAnswerCnt.value = 1
        _turn.value = false
        _timer.value = 100
        _timerActive.value = false
    }

    private fun startInterview() {
        viewModelScope.launch {
            for (i in 0 until 2) { //_interviews.value.size) {
                _index.value = i
                _reAnswerCnt.value = 1
                _turn.value = false

                // 질문 읽기 (TTS 완료 대기)
                val question = _interviews.value[_index.value].question
                playTTSAndWait(question)

                Log.d("InterviewingScreenViewModel", "Question ${_interviews.value[_index.value].interview_id}")

                // "10초 후 답변" 안내 (TTS 완료 대기)
                playTTSAndWait("10초 후 답변을 시작해주세요.")
                _timer.value = 10
                // 타이머 대기
                startTimer {

                }

                // 녹음 및 STT 처리
                val recordedText = startAnsweringProcessAndConvertToText()

                // 서버로 전송
                sendAnswerToServer(i, recordedText)

                // 다음 질문으로 이동하기 전에 잠시 대기
                delay(2000)
            }

            // 면접 종료
            interviewViewModel.setInterviewing(true)
            interviewViewModel.setLoading(true)
            baseViewModel.goScreen(ScreenType.INTERVIEWRESULT)
        }
    }

    // TTS를 실행하고 완료될 때까지 대기하는 함수
    private suspend fun playTTSAndWait(text: String) {
        val ttsCompleted = CompletableDeferred<Unit>()
        clovaTTS(text) {
            ttsCompleted.complete(Unit) // TTS 완료 신호 전달
        }
        ttsCompleted.await() // TTS 작업 완료 대기
    }

    private suspend fun startAnsweringProcessAndConvertToText(): String {
        val sttResult = CompletableDeferred<String?>()

        // 녹음 시작
        startRecording()
        _timer.value = 10
        _state.value = "STT"
        _turn.value = true

        // 타이머 대기
        startTimer {
            // 타이머가 완료되면 녹음 중지
            stopRecording()

            // MP4 → WAV 변환 후 STT 변환 실행
            audioFile?.let { mp4File ->
                val wavFile = File(context.cacheDir, "converted_audio.wav")
                convertToWav(mp4File, wavFile) { success ->
                    if (success) {
                        // 변환된 WAV 파일로 STT 실행
                        recognizeSpeech(wavFile) { result ->
                            sttResult.complete(result)
                        }
                    } else {
                        Log.e("InterviewingScreenViewModel", "WAV conversion failed.")
                        sttResult.complete("")
                    }
                }
            }
        }

        return sttResult.await() ?: ""
    }

    // 녹음 시작
    private fun startRecording() {
        try {
            audioFile = File(context.cacheDir, "audio_recording.mp4").apply {
                if (exists()) delete() // 기존 파일 삭제
            }

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile?.absolutePath)

                prepare()
                start()
            }

            Log.d("InterviewingScreenViewModel", "Recording started: ${audioFile?.absolutePath}")
        } catch (e: IOException) {
            Log.e("InterviewingScreenViewModel", "Error starting recording: ${e.message}")
        }
    }

    // 녹음 종료
    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            Log.d("InterviewingScreenViewModel", "Recording stopped.")

            // 파일 존재 여부 확인
            audioFile?.let { file ->
                if (file.exists()) {
                    Log.d("InterviewingScreenViewModel", "Recording file exists: ${file.absolutePath}")
                } else {
                    Log.d("InterviewingScreenViewModel", "Recording file does not exist.")
                }
            }
        } catch (e: Exception) {
            Log.e("InterviewingScreenViewModel", "Error stopping recording: ${e.message}")
        }
    }

    fun convertToWav(inputFile: File, outputFile: File, onConversionComplete: (Boolean) -> Unit) {
        if (outputFile.exists()) {
            outputFile.delete() // 기존 파일 삭제
        }
        val command = "-i ${inputFile.absolutePath} -acodec pcm_s16le -ar 16000 ${outputFile.absolutePath}"
        FFmpegKit.executeAsync(command) { session: Session ->
            if (session.returnCode.isValueSuccess) {
                Log.d("FFmpeg", "MP4 to WAV conversion succeeded: ${outputFile.absolutePath}")
                onConversionComplete(true)
            } else {
                Log.e("FFmpeg", "MP4 to WAV conversion failed.")
                onConversionComplete(false)
            }
        }
    }

    // Timer 함수 수정
    private suspend fun startTimer(onComplete: () -> Unit) {
        _timerActive.value = true
        while (_timer.value > 0 && _timerActive.value) {
            delay(1000) // 1초 대기
            _timer.value -= 1
        }
        _timerActive.value = false
        onComplete() // 타이머 완료 후 작업 실행
    }

    private fun recognizeSpeech(audioFile: File, onResult: (String?) -> Unit) {
        val retrofit = ClovaSTTClient.getSTTRetrofitClient(context)
        val api = retrofit.create(ClovaVoiceService::class.java)

        val requestBody = audioFile.asRequestBody("application/octet-stream".toMediaTypeOrNull())

        Log.d("ClovaSTT", "STT GOGO")

        // 음성 인식 API 호출
        val call = api.recognizeSpeech(requestBody)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val recognizedText = response.body()?.string() // 변환된 텍스트
                        Log.d("ClovaSTT", "Recognized text: $recognizedText")

                        // recognizedText가 JSON 형식으로 반환되므로 이를 파싱
                        val jsonObject = JsonParser.parseString(recognizedText).asJsonObject
                        val text = jsonObject.getAsJsonPrimitive("text").asString // "text" 필드만 추출

                        Log.d("ClovaSTT", "Extracted text: $text")

                        // 추출된 text를 onResult로 전달
                        onResult(text)
                    } catch (e: Exception) {
                        Log.e("ClovaSTT", "Error processing STT response: ${e.message}")
                        onResult(null)
                    }
                } else {
                    Log.e("ClovaSTT", "STT API failed: ${response.message()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ClovaSTT", "STT API call failed: ${t.message}")
                onResult(null)
            }
        })
    }

    private fun sendAnswerToServer(index: Int, answer: String) {
        viewModelScope.launch {
            try {
                val interview = _interviews.value[index]
                val request = InterviewAnswerRequest(answer)

                repository.sendInterviewAnswer(interview.interview_id, request)
                Log.d("InterviewingScreenViewModel", "Answer sent successfully")
            } catch (e: Exception) {
                Log.e("InterviewingScreenViewModel", "Error sending answer: ${e.message}")
            }
        }
    }



    fun clovaTTS(textForSpeach: String, function: () -> Boolean) {
        val retrofit = ClovaTTSClient.getTTSRetrofitClient(context)
        val api = retrofit.create(ClovaVoiceService::class.java)

        val call = api.synthesize(
            speaker = "nbora",
            speed = -1,
            volume = 2,
            pitch = 1,
            emotion = 2,
            alpha = 0,
            endPitch = 1,
            text = textForSpeach
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        // 임시 mp3 파일 생성 및 캐시 디렉토리에 저장
                        val tempMp3 = File.createTempFile("temp", "mp3", context.cacheDir)
                        tempMp3.deleteOnExit()

                        // 파일에 바이트 스트림 쓰기
                        response.body()?.byteStream()?.use { inputStream ->
                            FileOutputStream(tempMp3).use { fos ->
                                val buffer = ByteArray(1024)
                                var read: Int
                                while (inputStream.read(buffer).also { read = it } != -1) {
                                    fos.write(buffer, 0, read)
                                }
                                fos.flush()
                            }
                        }

                        // MediaPlayer를 사용해 음성 파일 재생
                        val mediaPlayer = MediaPlayer().apply {
                            setDataSource(tempMp3.path)
                            prepare()
                            start()

                            // 음성 재생이 끝났을 때 function 호출
                            setOnCompletionListener {
                                function() // TTS 재생 끝나면 true를 전달하는 function 호출
                            }
                        }

                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }

}