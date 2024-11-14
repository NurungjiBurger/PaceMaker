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
import com.maker.pacemaker.data.model.remote.AudioData
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
            // 테스트용 코드
            _questions.value = listOf("프로젝트에서 00기술을 사용하신 이유가 무엇인가요.", "해당 기술을 사용하면서 어떤 이슈가 있었나요?", "프로젝트 과정을 상세하게 설명해주세요.", "다시 프로젝트를 처음부터 시작한다면 어떤 기술을 추가하고 싶나요?", "프로젝트를 진행하면서 얻은 교훈이 무엇인가요?")
            interviewViewModel.setLoading(false)

            _answers.value = List(_questions.value.size) { "" }

            startInterview()
        }
    }

    private fun initialisze() {
        _reAnswerCnt.value = 1
        _turn.value = false
        _timer.value = 100
        _timerActive.value = false
    }

    private fun startInterview() {
        // 인덱스 기준으로 돌 것.
        _index.value = 0

        viewModelScope.launch {
            //for (i in 0 until _questions.value.size) {
                // 초기화
                initialisze()
                // 질문 읽어주기
                //clovaTTS(_questions.value[_index.value])

                // 10초 대기 후 답변 안내 TTS
                notifyTTS("10초 후 답변을 시작해주세요.")

                delay(3000)
                // 답변 시작 100초
                startAnsweringProcess()
                // 재답변 안내 후 10초 대기, 버튼이 눌린다면 다시 startAnsweringProcess()
                //clova("재답변을 원하시면 10초 내에 버튼을 눌러주세요.")
                //notifyTTS()
                delay(10000)
           // }
        }
    }

    private fun notifyTTS(text: String) {
        //clovaTTS(text)
        _state.value = "TTS"
        _timer.value = 2
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

            // 녹음이 끝난 후 STT 호출
            audioFile?.let { file ->
                val audioBytes = file.readBytes() // 녹음된 음성 파일을 바이트 배열로 읽기
                recognizeSpeech(audioBytes) // STT 처리
            }
        } catch (e: Exception) {
            Log.e("InterviewingScreenViewModel", "Error stopping recording: ${e.message}")
        }
    }

    private fun startAnsweringProcess() {
        viewModelScope.launch {
            _turn.value = true
            //_timer.value = 100
            _timer.value = 5
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

    private fun recognizeSpeech(audioBytes: ByteArray) {
        val retrofit = ClovaNetworkClient.getSTTRetrofitClient(context)
        val api = retrofit.create(ClovaVoiceService::class.java)

        val audioData = AudioData(audioBytes)

        // 음성 인식 API 호출
        val call = api.recognizeSpeech(audioData)
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