package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maker.pacemaker.data.model.remote.AnswerRequest
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.data.model.remote.reportRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


     //오늘의 문제
    private val _todayProblems = MutableStateFlow<List<Problem>>(emptyList())
    val todayProblems = _todayProblems

    // 오늘의 문제들 힌트
    private val _problemHints = MutableStateFlow<Map<Int, List<String>>>(emptyMap())
    val problemHints = _problemHints

     //풀어야 하는 문제 번호
    private val _nowProblemIndex = MutableStateFlow(
        baseViewModel.sharedPreferences.getInt("problemIndex", 0)
    )
    val nowProblemIndex = _nowProblemIndex

    // 맞춘 문제 수
    private val _todaySolvedCount = MutableStateFlow(
        baseViewModel.sharedPreferences.getInt("todaySolvedCount", 0)
    )
    val todaySolvedCount: MutableStateFlow<Int> get() = _todaySolvedCount

    // 틀린 문제 수
    private val _todayWrongCount = MutableStateFlow(
        baseViewModel.sharedPreferences.getInt("todayWrongCount", 0)
    )
    val todayWrongCount: MutableStateFlow<Int> get() = _todayWrongCount

    // 유저 정답
    private val _answer = MutableStateFlow("")
    val answer = _answer

    // 현재 틀린 횟수
    private val _wrongCnt = MutableStateFlow(0)
    val wrongCnt = _wrongCnt

    // 신고 내용
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
            fetchDailyProblems(currentDate)

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
            mainViewModel.setAllSolved(false)
        }
    }

    private fun fetchDailyProblems(currentDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            baseViewModel.setLoading(true)
            try {
                // 서버에서 데일리 문제를 가져옵니다.
                val problems = repository.getDailyProblem()

                Log.d("MainProblemSolveScreenViewModel", "Fetched daily problems: $problems")

                // 문제 ID를 추출하여 fetchProblemsByIds 호출
                val problemIds = problems.map { it.problem_id }
                fetchProblemsByIds(problemIds)

                // 문제 리스트를 상태에 저장합니다 (optional).
                // _todayProblems.value = problems

                // 문제 ID와 날짜를 저장합니다.
                saveProblemData(currentDate, problemIds)
            } catch (e: Exception) {
                Log.e("MainProblemSolveScreenViewModel", "Error fetching daily problems", e)
            } finally {
                baseViewModel.setLoading(false)
            }
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
                    val hints = repository.getProblemHints(problemId).map { it.hint }
                    hintsMap[problemId] = hints
                } catch (e: Exception) {
                    Log.e("MainProblemSolveScreenViewModel", "Error fetching problem ID: $problemId", e)
                }
            }

            _todayProblems.value = problemsList
            _problemHints.value = hintsMap

            Log.d("MainProblemSolveScreenViewModel", "Fetched problems: $problemsList")
            Log.d("MainProblemSolveScreenViewModel", "Fetched hints: $hintsMap")

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
                        _todayProblems.value[_nowProblemIndex.value].problem_id,
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

    fun onSkip() {
        viewModelScope.launch(Dispatchers.IO) {
            // 현재 문제를 가져옵니다.
            val currentProblem = _todayProblems.value[_nowProblemIndex.value]

            // word 필드에서 answer JSON 문자열을 추출합니다.
            val wordJson = currentProblem.word

            // Gson을 사용하여 JSON 파싱을 수행합니다.
            val gson = Gson()
            val jsonObject = gson.fromJson(wordJson, JsonObject::class.java)

            // answer 배열을 가져오고 첫 번째 답변을 추출합니다.
            val firstAnswer = jsonObject.getAsJsonArray("answer").firstOrNull()?.asString ?: "정답이 없습니다."

            withContext(Dispatchers.Main) {
                // 진동과 토스트 메시지를 표시합니다.
                baseViewModel.triggerVibration()
                baseViewModel.triggerToast("정답은 $firstAnswer 입니다. 2초 후 다음 문제로 넘어갑니다.")
                _answer.value = firstAnswer
            }

            delay(2000)

            // 다음 문제로 넘어가기 위한 로직
            _answer.value = ""
            _nowProblemIndex.value += 1
            _todayWrongCount.value += 1
            saveCnts()
            _wrongCnt.value = 0
        }
    }

    fun onSubmit() {
        Log.d("MainProblemSolveScreenViewModel", "Submitting answer")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val answerRequest = AnswerRequest(_answer.value)
                val uId = baseViewModel.sharedPreferences.getString("fireBaseUID", "")
                val currentProblem = _todayProblems.value[_nowProblemIndex.value]

                Log.d("MainProblemSolveScreenViewModel", "Current problem: $currentProblem")
                Log.d("MainProblemSolveScreenViewModel", "Current answer: ${_answer.value}")

                val answerResponse = uId?.let {
                    Log.d("MainProblemSolveScreenViewModel", "Submitting answer: $it")
                    repository.solveProblem(it, currentProblem.problem_id, answerRequest)
                }

                _answer.value = ""
                Log.d("MainProblemSolveScreenViewModel", "Answer response: $answerResponse")

                if (answerResponse?.result == true) {
                    _nowProblemIndex.value += 1
                    _todaySolvedCount.value += 1
                    saveCnts()
                    _wrongCnt.value = 0
                    _report.value = ""
                } else {
                    _wrongCnt.value += 1
                    handleIncorrectAnswer()
                }
            } catch (e: Exception) {
                Log.e("MainProblemSolveScreenViewModel", "Error submitting answer", e)
            }
        }
    }

    private fun saveCnts() {
        baseViewModel.sharedPreferences.edit().apply {
            putInt("problemIndex", _nowProblemIndex.value)
            putInt("todaySolvedCount", _todaySolvedCount.value)
            putInt("todayWrongCount", _todayWrongCount.value)
            apply()
        }
    }

    private suspend fun handleIncorrectAnswer() {
        withContext(Dispatchers.Main) {
            baseViewModel.triggerVibration()
            baseViewModel.triggerToast("오답입니다. 다시 시도해주세요.")
        }
    }
}
