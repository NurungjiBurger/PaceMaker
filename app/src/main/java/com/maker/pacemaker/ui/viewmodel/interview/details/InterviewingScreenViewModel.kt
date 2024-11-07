package com.maker.pacemaker.ui.viewmodel.interview.details

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.OpenAIRequest
import com.maker.pacemaker.data.model.remote.OpenAIService
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class InterviewingScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel,
    private val openAIService: OpenAIService
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val interviewViewModel = base

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
            try {
// OpenAI API 호출
                val promptText = """
                "${interviewViewModel.text.value}" 을 바탕으로 나올 수 있는 기술 면접 질문 5개를 생성해주세요.
                질문은 가능한 한 면접 대상자의 기술적 능력을 평가할 수 있는 내용으로 작성해 주세요.
            """.trimIndent()

                val response = openAIService.getQuestions(
                    OpenAIRequest(
                        prompt = promptText,
                        max_tokens = 150, // 질문이 길어질 수 있으므로 토큰 제한 조정
                        n = 5             // 원하는 질문 개수
                    )
                )

                if (response.isSuccessful) {
                    val generatedQuestions = response.body()?.choices?.map { it.text.trim() } ?: listOf()
                    _questions.value = generatedQuestions
                } else {
                    Log.e("InterviewViewModel", "API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("InterviewViewModel", "Error fetching questions: ${e.message}")
            }
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
                questionTTS()
                // 10초 대기 후 답변 안내 TTS
                notifyTTS()
                delay(10000)
                // 답변 시작 100초
                startAnsweringProcess()
                // 재답변 안내 후 10초 대기, 버튼이 눌린다면 다시 startAnsweringProcess()
                notifyTTS()
                delay(10000)
            }
        }
    }
    
    private fun questionTTS() {
        // index 질문 읽어주기
    }

    private fun notifyTTS() {
        // 10초 대기 안내 TTS
    }

    private fun answerSTT() {

    }

    private fun startAnsweringProcess() {
        viewModelScope.launch {
            _turn.value = true
            _timer.value = 100
            _timerActive.value = true
            startTimer()
            // 면접자에게 답변을 받기 시작
            answerSTT()
        }
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


}