package com.maker.pacemaker.ui.viewmodel.interview.details

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.ClovaNetworkClient
import com.maker.pacemaker.data.model.remote.ClovaVoiceService
import com.maker.pacemaker.data.model.remote.OpenAIRequest
import com.maker.pacemaker.data.model.remote.OpenAIService
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.Session
import com.maker.pacemaker.data.model.remote.CV
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@HiltViewModel
open class InterviewingScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel,
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val interviewViewModel = base

    val context = baseViewModel.context

    val repository = baseViewModel.repository

    // 자기소개서 번호
    private val _cvId = MutableStateFlow(0)
    val cvId = _cvId

    // 질문 리스트 (AI 생성 질문)
    private val _questions = MutableStateFlow(listOf<String>())
    val questions = _questions

    // 답변 리스트
    private val _answers = MutableStateFlow(listOf<String>())
    val answers = _answers

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
    private val _timer = MutableStateFlow(100)
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
        // 서버와 통신해서 질문 받아오기
        viewModelScope.launch {

            interviewViewModel.setLoading(true)

            // 특수문자 및 줄바꿈을 이스케이프 처리한 자기소개서 텍스트
            val sanitizedText = sanitizeText(interviewViewModel.text.value)
            val request = CV(sanitizedText)

            val response =
                try {
                    repository.sendCV(request)
                } catch (e: Exception) {
                    Log.e("InterviewingScreenViewModel", "Error sending CV: ${e.message}")
                    return@launch
                }

            Log.d("InterviewingScreenViewModel", "CV sent: ${response}")

            // CV 준비 상태 확인
            val isReady = try {
                waitForCVReady(response.cv_id)
            } catch (e: Exception) {
                Log.e("InterviewingScreenViewModel", "Error checking CV ready: ${e.message}")
                interviewViewModel.setLoading(false)
                return@launch
            }

            if (isReady) {
                interviewViewModel.setLoading(false)

                _answers.value = List(_questions.value.size) { "" }
                startInterview()
            }
        }
    }

    // CV 준비 상태를 주기적으로 확인하는 함수
    private suspend fun waitForCVReady(cvId: Int): Boolean {
        var isReady = false
        val maxRetries = 10
        val delayMillis = 2000L // 2초 간격으로 재시도

        repeat(maxRetries) {
            val response = repository.checkCVReady(cvId)

            Log.d("InterviewingScreenViewModel", "CV ready response: ${response.ready}")

            if (response.ready) {
                isReady = true
                return@repeat
            }
            delay(delayMillis) // 다음 요청 전 대기
        }

        return isReady
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
        // 인덱스 기준으로 질문 리스트를 순차적으로 돌며 실행
        viewModelScope.launch {
            for (i in 0 until _questions.value.size) {
                // 현재 질문 인덱스 초기화 및 질문 읽기
                _index.value = i
                _reAnswerCnt.value = 1
                _turn.value = false
                _state.value = "TTS"

                // TTS로 질문 읽기
                clovaTTS(_questions.value[_index.value])
                delay(3000) // TTS 질문이 끝날 시간을 기다림

                // 대기 메시지 출력 후 대기 시간 10초 설정
                notifyTTS("10초 후 답변을 시작해주세요.", 10)
                delay(10000) // 10초의 대기 시간

                // 답변 녹음 프로세스 시작
                startAnsweringProcess()

                // 대기 중 사용자가 재답변 버튼을 누를 수 있는 시간 제공 (10초)
                delay(10000)
                if (_reAnswerCnt.value > 0) {
                    notifyTTS("재답변을 원하시면 10초 내에 버튼을 눌러주세요.", 10)
                    delay(10000) // 10초 동안 대기하며 재답변 대기

                    if (_reAnswerCnt.value > 0) {
                        // 재답변이 선택된 경우 답변 프로세스를 재개
                        startAnsweringProcess()
                    }
                }

                // STT로 녹음된 답변을 텍스트로 변환
                recognizeSpeech(audioFile!!)

                // 다음 질문으로 이동하기 위해 잠시 대기
                delay(2000)
            }
        }
    }

//    private fun startInterview() {
//        // 인덱스 기준으로 돌 것.
//        _index.value = 0
//
//        viewModelScope.launch {
//            //for (i in 0 until _questions.value.size) {
//                // 초기화
//                initialisze()
//                // 질문 읽어주기
//                //clovaTTS(_questions.value[_index.value])
//
//                // 10초 대기 후 답변 안내 TTS
//                notifyTTS("10초 후 답변을 시작해주세요.", 10)
//
//                delay(3000)
//                // 답변 시작 100초
//                startAnsweringProcess()
//                // 재답변 안내 후 10초 대기, 버튼이 눌린다면 다시 startAnsweringProcess()
//                //clova("재답변을 원하시면 10초 내에 버튼을 눌러주세요.")
//                //notifyTTS()
//                delay(10000)
//           // }
//        }
//    }

    private fun notifyTTS(text: String, time: Int) {
        clovaTTS(text)
        _state.value = "TTS"
        _timer.value = time
        resumeTimer()
    }

    // 녹음 시작
    private fun startRecording() {
        try {
            // 녹음할 파일 경로 설정
            audioFile = File(context.cacheDir, "audio_recording.mp4")

            // MediaRecorder 설정
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile?.absolutePath)

                prepare()
                start()
            }

            Log.d("InterviewingScreenViewModel", "Recording started.")
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

                    // MP4 파일을 WAV로 변환
                    val outputFile = File(context.cacheDir, "converted_audio.wav")
                    convertToWav(file, outputFile) { conversionSuccess ->
                        if (conversionSuccess) {
                            // WAV 파일을 STT API로 전송
                            recognizeSpeech(outputFile)
                        } else {
                            Log.e("InterviewingScreenViewModel", "Conversion failed")
                        }
                    }
                } else {
                    Log.d("InterviewingScreenViewModel", "Recording file does not exist.")
                }
            }

            // 녹음이 완료된 후 파일 길이 확인
            audioFile?.let { file ->
                Log.d("InterviewingScreenViewModel", "Recording length: ${file.length()} bytes")
                if (file.exists()) {
                    Log.d("InterviewingScreenViewModel", "Recording file exists: ${file.absolutePath}")
                    // 파일 내용이 충분한지 확인
                }
            }
        } catch (e: Exception) {
            Log.e("InterviewingScreenViewModel", "Error stopping recording: ${e.message}")
        }
    }

    fun convertToWav(inputFile: File, outputFile: File, onConversionComplete: (Boolean) -> Unit) {
        if (outputFile.exists()) {
            outputFile.delete()
        }
        // FFmpegKit 초기화 및 변환 명령어 실행
        val command = "-i ${inputFile.absolutePath} ${outputFile.absolutePath}"
        FFmpegKit.executeAsync(command) { session: Session ->
            // 세션 상태 확인
            val returnCode = session.returnCode
            if (returnCode.isValueSuccess) {
                Log.d("FFmpeg", "Conversion succeeded")
                onConversionComplete(true)
            } else {
                Log.e("FFmpeg", "Conversion failed")
                onConversionComplete(false)
            }
        }
    }

    private fun startAnsweringProcess() {
        viewModelScope.launch {
            _turn.value = true
            //_timer.value = 100
            _timer.value = 10
            _state.value = "STT"
            // 면접자에게 답변을 받기 시작
            startRecording()
            resumeTimer()
        }
    }

    private fun resumeTimer() {
        viewModelScope.launch {
            _timerActive.value = true
            startTimer()
        }
    }

    private fun pauseTimer() {
        viewModelScope.launch {
            _timerActive.value = false
        }
    }

    // Timer 함수 수정
    private fun startTimer() {
        viewModelScope.launch {
            // 타이머가 활성화될 때만 실행하도록 설정
            Log.d("InterviewingScreenViewModel", "Timer started.")

            while (_timer.value > 0 && _timerActive.value) {  // 타이머가 활성화된 상태에서만 계속 실행
                delay(1000)  // 1초 간격으로 반복
                _timer.value -= 1  // 타이머 감소

                // 1초마다 타이머 값 확인 로그 출력
                Log.d("Timer", "Timer value: ${_timer.value}")

                if (_timer.value <= 0) {
                    if (_state.value == "STT") stopRecording()  // 타이머가 끝나면 녹음을 중지
                }
            }

            // 타이머가 비활성화되었을 때는 종료
            if (!_timerActive.value) {
                Log.d("InterviewingScreenViewModel", "Timer paused.")
            }
        }
    }

    private fun recognizeSpeech(audioFile: File) {
        val retrofit = ClovaNetworkClient.getSTTRetrofitClient(context)
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

                        // 텍스트로 변환된 결과를 현재 인덱스에 저장
                        val updatedAnswers = _answers.value.toMutableList()
                        if (recognizedText != null) {
                            updatedAnswers[_index.value] = recognizedText
                            _answers.value = updatedAnswers
                        }
                    } catch (e: Exception) {
                        Log.e("ClovaSTT", "Error processing STT response: ${e.message}")
                    }
                } else {
                    Log.e("ClovaSTT", "STT API failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ClovaSTT", "STT API call failed: ${t.message}")
            }
        })
    }

    fun clovaTTS(textForSpeach: String) {
        val retrofit = ClovaNetworkClient.getTTSRetrofitClient(context)
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