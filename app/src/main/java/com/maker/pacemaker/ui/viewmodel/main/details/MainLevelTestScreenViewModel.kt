package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.remote.AnswerRequest
import com.maker.pacemaker.data.model.remote.LevelRequest
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class MainLevelTestScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base

    val repository = baseViewModel.repository

    // 제한 시간
    private val _timeLimit = MutableStateFlow(60)
    val timeLimit = _timeLimit

    // 현재 단계
    private val _level = MutableStateFlow(1)
    val level = _level

    // 현재 단계의 테스트를 위한 문제
    private val _testProblems = MutableStateFlow<List<Problem>>(listOf())
    val testProblems: MutableStateFlow<List<Problem>> get() = _testProblems

    // 현재 풀고 있는 문제 인덱스
    private val _nowProblemIndex = MutableStateFlow(0)
    val nowProblemIndex: MutableStateFlow<Int> get() = _nowProblemIndex

    // 푼 문제 수
    private val _testSolvedCount = MutableStateFlow(0)
    val testSolvedCount: MutableStateFlow<Int> get() = _testSolvedCount

    // 틀린 문제 수
    private val _testWrongCount = MutableStateFlow(0)
    val testWrongCount: MutableStateFlow<Int> get() = _testWrongCount

    // 유저 정답
    private val _answer = MutableStateFlow("")
    val answer = _answer

    // 현재 틀린 횟수
    private val _wrongCnt = MutableStateFlow(0)
    val wrongCnt = _wrongCnt

    // 동그라미 표시 상태 (0: 시도하지 않음, 1: 정답, 2: 오답)
    private val _circleStates = MutableStateFlow<List<Int>>(listOf())
    val circleStates = _circleStates

    // 레벨 테스트 완료
    private val _clear = MutableStateFlow(false)
    val clear = _clear

    // 페이지 멘트
    private val _ment = MutableStateFlow("")
    val ment = _ment

    init{
        if (baseViewModel.sharedPreferences.getString("leveltestdate", "") != getCurrentDate()) {
            restate()
            startTimeLimitCountdown()
        } else {
            _ment.value = "오늘은 이미 진단을 완료하셨습니다."
            _clear.value = true
        }
    }

    fun restate() {
        if (_clear.value) {
            _clear.value = false
            _ment.value = "오늘은 이미 진단을 완료하셨습니다."
            _clear.value = true
        } else {
            initTest()
        }
    }

    // 문제를 초기화하고 문제 목록을 가져오는 함수
    fun initTest() {
        viewModelScope.launch {
            val fetchedProblems = fetchProblemByLevel(level.value)

            Log.d("MainLevelTestScreenViewModel", "Fetched problems: $fetchedProblems")

            _testProblems.value = fetchedProblems
            _circleStates.value = List(_testProblems.value.size) { 0 }
            _timeLimit.value = 60
            _nowProblemIndex.value = 0
            _testSolvedCount.value = 0
            _testWrongCount.value = 0
            _wrongCnt.value = 0
            _answer.value = ""
        }
    }


    // 문제를 레벨에 따라 가져오는 함수
    suspend fun fetchProblemByLevel(level: Int): List<Problem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = repository.getLevelTestProblemsByLevel(level)
                // 데이터가 존재하면 반환
                if (response.isNotEmpty()) {
                    return@withContext response
                } else {
                    Log.e("MainLevelTestScreenViewModel", "No problems found")
                    return@withContext listOf<Problem>() // 비어있는 리스트 반환
                }
            } catch (e: Exception) {
                Log.e("MainLevelTestScreenViewModel", "Error fetching problems", e)
                return@withContext listOf<Problem>() // 예외 발생 시 비어있는 리스트 반환
            }
        }
    }

    // 문제 변경 시 사용자 답안 변경 감지
    fun onAnswerChanged(answer: String) {
        _answer.value = answer
    }

    // 정답 제출 처리
    fun onSubmit() {
        Log.d("MainLevelTestScreenViewModel", "Submitting answer")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val answerRequest = AnswerRequest(_answer.value)
                val currentProblem = testProblems.value[nowProblemIndex.value]
                val uId = base.baseViewModel.sharedPreferences.getString("fireBaseUID", "")

                Log.d("MainLevelTestScreenViewModel", "Current problem: $currentProblem")
                Log.d("MainLevelTestScreenViewModel", "Current answer: ${_answer.value}")

                // 문제 제출 처리 (서버와 통신)
                val answerResponse = uId?.let {
                    repository.solveProblem(it, currentProblem.problem_id, answerRequest)
                }

                _answer.value = "" // 답안 초기화

                if (answerResponse?.result == true) {
                    // 정답 처리
                    _testSolvedCount.value += 1
                    updateCircleState(1)
                    _nowProblemIndex.value += 1
                    _wrongCnt.value = 0
                    _timeLimit.value = 60 // 정답시 제한시간 초기화
                } else {
                    // 오답 처리
                    _wrongCnt.value += 1
                    if (_wrongCnt.value >= 3) {
                        _testWrongCount.value += 1
                        updateCircleState(2)
                        _nowProblemIndex.value += 1
                        _wrongCnt.value = 0
                        _timeLimit.value = 60 // 틀린 문제는 시간 초기화
                    } else {
                        _timeLimit.value += 30 // 오답시 제한시간 증가
                        handleIncorrectAnswer()
                    }
                }

                if (_nowProblemIndex.value >= testProblems.value.size) {
                    // 모든 문제를 푼 경우
                    judgeUserLevel()
                }

            } catch (e: Exception) {
                Log.e("MainLevelTestScreenViewModel", "Error submitting answer", e)
            }
        }
    }

    private fun judgeUserLevel() {
        // 레벨 판정 로직
        // 맞은 문제 수가 전체 문제 수를 기준으로 / 2.0f로 나누고 반올림 했을 때 보다 크거나 같으면 레벨 업
        val pivot = testProblems.value.size / 2.0f

        if (_level.value == 5) finishLevelTest()

        if (testSolvedCount.value >= pivot) {
            // 레벨 업
            _level.value += 1
            restate()
        } else {
            finishLevelTest()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun finishLevelTest() {
        baseViewModel.editor.putString("leveltestdate", getCurrentDate())
        baseViewModel.editor.apply()
        // 레벨 패치
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                _ment.value = "진단이 완료되었습니다.\n${baseViewModel.userInfo.value.nickname}님의 레벨은 ${_level.value} 입니다."
                _clear.value = true
                changeScreen()
            }
            val request = LevelRequest(_level.value)
            repository.updateLevel(request)
        }
    }

    fun changeScreen() {
        viewModelScope.launch(Dispatchers.Main) {
            baseViewModel.triggerToast("진단이 완료되었습니다. 뒤로가기를 눌러 화면을 이동해주세요.")
        }
    }

    // 동그라미 상태 업데이트 함수
    private fun updateCircleState(state: Int) {
        _circleStates.value = _circleStates.value.toMutableList().apply {
            this[nowProblemIndex.value] = state
        }
    }

    private suspend fun handleIncorrectAnswer() {
        withContext(Dispatchers.Main) {
            base.baseViewModel.triggerVibration()
            base.baseViewModel.triggerToast("오답입니다. 다시 시도해주세요.")
        }
    }

    // 제한시간을 1초에 1씩 줄여나가는 함수
    fun startTimeLimitCountdown() {
        viewModelScope.launch(Dispatchers.Main) {
            // 1초마다 timeLimit을 감소
            while (_timeLimit.value > 0) {
                delay(1000)  // 1초 대기
                _timeLimit.value -= 1
            }
            // timeLimit이 0이 되면 실행될 로직
            finishLevelTest()
        }
    }
}