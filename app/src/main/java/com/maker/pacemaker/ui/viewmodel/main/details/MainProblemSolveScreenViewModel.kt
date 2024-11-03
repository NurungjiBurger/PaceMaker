package com.maker.pacemaker.ui.viewmodel.main.details

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.AnswerRequest
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.data.model.remote.ProblemHintResponse
import com.maker.pacemaker.data.model.remote.reportRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class MainProblemSolveScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base.baseViewModel

    val repository = baseViewModel.repository

    // 오늘의 문제                                           정답 ,   문제 설명
    private val _todayProblems = MutableStateFlow<List<Problem>>(emptyList())
    val todayProblems = _todayProblems

    private val _problemHints = MutableStateFlow<Map<Int, List<String>>>(emptyMap())
    val problemHints = _problemHints

    private val _todaySolvedCount = MutableStateFlow(baseViewModel.sharedPreferences.getInt("todaySolvedCount", 0))
    val todaySolvedCount: MutableStateFlow<Int> get() = _todaySolvedCount

    private val _answer = MutableStateFlow("")
    val answer = _answer

    private val _wrongCnt = MutableStateFlow(0)
    val wrongCnt = _wrongCnt

    private val _report = MutableStateFlow("")
    val report = _report

    init {
        fetchRandomProblems()
    }

    private fun fetchRandomProblems() {
        viewModelScope.launch(Dispatchers.IO) {
            val problemsList = mutableListOf<Problem>()
            val hintsMap = mutableMapOf<Int, List<String>>()

            val dailycnt = baseViewModel.sharedPreferences.getInt("myDailyCount", 30)

            // 10개의 랜덤 문제 번호 생성
            val randomProblemIds = (1..150).shuffled().take(dailycnt)
            Log.d("MainProblemSolveScreenViewModel", "Fetching problems for IDs: $randomProblemIds")

            randomProblemIds.forEach { problemId ->
                try {
                    Log.d("MainProblemSolveScreenViewModel", "Fetching problem ID: $problemId")

                    // 문제 가져오기
                    val problemResponse = repository.getProblemById(problemId)
                    problemsList.add(problemResponse)

                    // 해당 문제의 힌트 가져오기
                    val hintResponse: ProblemHintResponse = repository.getProblemHints(problemId)
                    hintsMap[problemId] = hintResponse.hints
                    Log.d("MainProblemSolveScreenViewModel", "Fetched hints for problem ID $problemId: ${hintResponse.hints}")

                } catch (e: Exception) {
                    Log.e("MainProblemSolveScreenViewModel", "Error fetching problem ID: $problemId", e)
                }
            }

            // 가져온 문제와 힌트를 StateFlow에 저장
            _todayProblems.value = problemsList
            _problemHints.value = hintsMap
        }
    }

    fun onAnswerChanged(answer: String) {
        _answer.value = answer
    }

    fun onReportChanged(report: String) {
        _report.value = report
    }

    fun onReport() {
        viewModelScope.launch(Dispatchers.IO) {
            // 문제 신고
            val reportRequest = reportRequest(1, _report.value)
            val reportResponse = repository.reportProblem(
                _todayProblems.value[_todaySolvedCount.value].problem_id,
                reportRequest
            )

            withContext(Dispatchers.Main) {
                if (reportResponse.message.contains("success")) {
                    baseViewModel.triggerToast("신고가 접수되었습니다.")
                } else {
                    baseViewModel.triggerToast("신고에 실패했습니다.")
                }
            }
        }
    }

    fun onSubmit() {
        // 정답 제출, 서버 통신
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val answerRequest = AnswerRequest(_answer.value)
                val answerResponse = repository.solveProblem(1, _todayProblems.value[_todaySolvedCount.value].problem_id, answerRequest)
                // 정답 확인
                if (answerResponse.result) {
                    _todaySolvedCount.value += 1
                    _answer.value = ""
                    _wrongCnt.value = 0
                } else {
                    // 사용자에게 오답임을 알림
                    withContext(Dispatchers.Main) {
                        baseViewModel.triggerVibration()
                        baseViewModel.triggerToast("오답입니다. 다시 시도해주세요.")
                    }
                    // 추가로 힌트 개방
                    if (_wrongCnt.value < 3) _wrongCnt.value += 1
                }
            } catch (e: Exception) {
                // 에러 처리 (예: 서버 통신 실패)
            }
        }
    }

}