package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class MainLevelTestScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base

    private val _nowProblem = MutableStateFlow("객체지향의 SOLID 중 “클래스는 단 하나의 목적을 가져야 하며, 클래스를 변경하는 이유는 단 하나의 이유여야 한다.”는 원칙은 ?")
    val nowProblem: MutableStateFlow<String> get() = _nowProblem

    private val _userResponse = MutableStateFlow(TextFieldValue())
    val userResponse: MutableStateFlow<TextFieldValue> get() = _userResponse

    // 전화번호 필드 변화 감지
    fun onUserResponseChange(newValue: TextFieldValue) {
        _userResponse.value = newValue
    }

    fun onUserResponseSubmit() {
        // 사용자 응답 제출
        if (_userResponse.value.text.isNotEmpty()) {
            // 사용자 응답이 비어 있지 않은 경우
            // 사용자 응답을 서버로 전송
            if (_userResponse.value.text == "SRP") {
                // 정답인 경우
                // 다음 문제로 이동
                _nowProblem.value = "객체지향의 SOLID 중 “개방 폐쇄 원칙”은 무엇인가요?"
                _userResponse.value = TextFieldValue()
            } else {
                // 오답인 경우
                // 사용자에게 오답임을 알림
                _userResponse.value = TextFieldValue()
                baseViewModel.baseViewModel.triggerVibration()
            }
        }
    }


}