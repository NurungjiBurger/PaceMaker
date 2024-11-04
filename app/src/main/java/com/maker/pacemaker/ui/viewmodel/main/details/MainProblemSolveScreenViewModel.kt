package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.AnswerRequest
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.data.model.remote.reportRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainProblemSolveScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base
    val repository = baseViewModel.repository

    private val _todayProblems = MutableStateFlow<List<Problem>>(emptyList())
    val todayProblems = _todayProblems

    private val _problemHints = MutableStateFlow<Map<Int, List<String>>>(emptyMap())
    val problemHints = _problemHints

    private val _todaySolvedCount = MutableStateFlow(
        baseViewModel.sharedPreferences.getInt("todaySolvedCount", 0)
    )
    val todaySolvedCount: MutableStateFlow<Int> get() = _todaySolvedCount

    private val _answer = MutableStateFlow("")
    val answer = _answer

    private val _wrongCnt = MutableStateFlow(0)
    val wrongCnt = _wrongCnt

    private val _report = MutableStateFlow("")
    val report = _report

    init {
        handleDailyProblemLoad()
    }

    /**
     * 오늘의 문제를 로드하는 초기화 로직.
     * 날짜를 비교하여 새로운 문제를 가져올지, 저장된 문제를 사용할지 결정합니다.
     */
    private fun handleDailyProblemLoad() {
        val currentDate = getCurrentDate()
        val savedDate = baseViewModel.sharedPreferences.getString("date", "")

        if (currentDate != savedDate) {
            Log.d("MainProblemSolveScreenViewModel", "New date detected: $currentDate")
            fetchRandomProblems(currentDate)
        } else {
            Log.d("MainProblemSolveScreenViewModel", "Loading saved problems")
            loadSavedProblems()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * `SharedPreferences`에 문제 번호 리스트와 날짜를 저장합니다.
     */
    private fun saveProblemData(date: String, problemIds: List<Int>) {
        baseViewModel.sharedPreferences.edit().apply {
            putString("date", date)
            putString("problems", problemIds.joinToString(","))
            apply()
        }
    }

    /**
     * `SharedPreferences`에서 문제 번호 리스트를 불러옵니다.
     */
    private fun loadSavedProblems() {
        val problemIds = baseViewModel.sharedPreferences.getString("problems", "")
            ?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

        Log.d("MainProblemSolveScreenViewModel", "Loaded problem IDs: $problemIds")

        if (problemIds.isNotEmpty()) {
            fetchProblemsByIds(problemIds)
        }
    }

    /**
     * 랜덤한 문제들을 가져오고 `SharedPreferences`에 저장합니다.
     */
    private fun fetchRandomProblems(currentDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            baseViewModel.setLoading(true)
            try {
                val dailyCount = baseViewModel.sharedPreferences.getInt("myDailyCount", 1)
                val randomProblemIds = (1..254).shuffled().take(dailyCount)

                fetchProblemsByIds(randomProblemIds)

                saveProblemData(currentDate, randomProblemIds)
            } catch (e: Exception) {
                Log.e("MainProblemSolveScreenViewModel", "Error fetching random problems", e)
            }

            baseViewModel.setLoading(false)
        }
    }

    /**
     * 서버에서 지정된 문제 ID에 해당하는 문제를 가져옵니다.
     */
    private fun fetchProblemsByIds(problemIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            baseViewModel.setLoading(true)

            val problemsList = mutableListOf<Problem>()
            val hintsMap = mutableMapOf<Int, List<String>>()

            problemIds.forEach { problemId ->
                try {
                    val problem = repository.getProblemById(problemId)
                    problemsList.add(problem)
                    // 힌트 로딩 로직 (생략 가능)
                    // hintsMap[problemId] = repository.getProblemHints(problemId).hints
                } catch (e: Exception) {
                    Log.e("MainProblemSolveScreenViewModel", "Error fetching problem ID: $problemId", e)
                }
            }

            _todayProblems.value = problemsList

            baseViewModel.setLoading(false)
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
            val uId = baseViewModel.sharedPreferences.getString("fireBaseUID", "")
            val reportRequest = uId?.let { reportRequest(it, _report.value) }
            try {
                val reportResponse = reportRequest?.let {
                    repository.reportProblem(
                        _todayProblems.value[_todaySolvedCount.value].problem_id,
                        it
                    )
                }
                withContext(Dispatchers.Main) {
                    if (reportResponse != null) {
                        if (reportResponse.message.contains("success")) {
                            baseViewModel.triggerToast("신고가 접수되었습니다.")
                        } else {
                            baseViewModel.triggerToast("신고에 실패했습니다.")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MainProblemSolveScreenViewModel", "Report failed", e)
            }
        }
    }

    fun onSubmit() {
        Log.d("MainProblemSolveScreenViewModel", "Submitting answer")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val answerRequest = AnswerRequest(_answer.value)
                val uId = baseViewModel.sharedPreferences.getString("fireBaseUID", "")
                val currentProblem = _todayProblems.value[_todaySolvedCount.value]

                Log.d("MainProblemSolveScreenViewModel", "Current problem: $currentProblem")
                Log.d("MainProblemSolveScreenViewModel", "Current answer: ${_answer.value}")

                val answerResponse = uId?.let {
                    Log.d("MainProblemSolveScreenViewModel", "Submitting answer: $it")
                    repository.solveProblem(it, currentProblem.problem_id, answerRequest)
                }

                _answer.value = ""
                Log.d("MainProblemSolveScreenViewModel", "Answer response: $answerResponse")

                if (answerResponse?.result == true) {
                    // Update the count of solved problems
                    _todaySolvedCount.value += 1
                    // Save the updated count to SharedPreferences
                    saveSolvedCount(_todaySolvedCount.value)
                    _wrongCnt.value = 0
                } else {
                    handleIncorrectAnswer()
                }
            } catch (e: Exception) {
                Log.e("MainProblemSolveScreenViewModel", "Error submitting answer", e)
            }
        }
    }

    /**
     * Save the number of problems solved to SharedPreferences.
     */
    private fun saveSolvedCount(count: Int) {
        baseViewModel.sharedPreferences.edit().apply {
            putInt("todaySolvedCount", count)
            apply()
        }
    }

    private suspend fun handleIncorrectAnswer() {
        withContext(Dispatchers.Main) {
            baseViewModel.triggerVibration()
            baseViewModel.triggerToast("오답입니다. 다시 시도해주세요.")
        }
        if (_wrongCnt.value < 3) _wrongCnt.value += 1
    }
}
