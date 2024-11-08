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

    init {
        // AI 질문 생성 요청 (예시)
        fetchQuestionsFromAI()
        _timerActive.value = false
        startTimer()
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
            for (i in 0 until _questions.value.size) {
                // 초기화
                initialisze()
                // 질문 읽어주기
                //questionTTS()
                // 10초 대기 후 답변 안내 TTS
                clova("10초 후 답변을 시작해주세요.")
                delay(10000)
                // 답변 시작 100초
                //startAnsweringProcess()
                // 재답변 안내 후 10초 대기, 버튼이 눌린다면 다시 startAnsweringProcess()
                //clova("재답변을 원하시면 10초 내에 버튼을 눌러주세요.")
                //notifyTTS()
                delay(10000)
            }
        }
    }

    // 질문 읽어주기 (TTS)
    private fun questionTTS() {
        val questionText = _questions.value[_index.value]
        clova(questionText)  // 클로바 TTS 호출
    }

    private fun answerSTT() {

    }

    private fun startAnsweringProcess() {
        viewModelScope.launch {
            _turn.value = true
            _timer.value = 100
            _timerActive.value = true
            // 면접자에게 답변을 받기 시작
            answerSTT()
        }
    }

    private fun resumeTimer() {
        _timerActive.value = true
    }

    private fun pauseTimer() {
        _timerActive.value = false
    }

    private fun startTimer() {
        viewModelScope.launch {
            if (_timerActive.value) {
                while (_timer.value > 0) {
                    delay(1000)
                    _timer.value -= 1
                }

                // 시간이 다 되었을 경우, startInterview()의 startAnsweringProcess()다음으로 넘어가야 함.
            }
        }
    }

    fun clova(textForSpeach: String) {
        val retrofit = ClovaNetworkClient.getRetrofitClient(context)
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