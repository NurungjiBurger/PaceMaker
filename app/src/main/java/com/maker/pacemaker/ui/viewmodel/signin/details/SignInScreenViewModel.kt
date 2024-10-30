package com.maker.pacemaker.ui.viewmodel.signin.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.MyApplication
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SignInScreenViewModel @Inject constructor(
    val base: SignUpBaseViewModel
) : ViewModel() {

    val baseViewModel = base

    private var _errorMessage: String? = null
    val errorMessage: String?
        get() = _errorMessage

    private val _isLoggedIn = MutableLiveData<Boolean>() // 로그인 상태 LiveData
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    var coreectEmail = false

    fun checkEmail(email: String) {
        // 이메일 형식을 확인하는 정규 표현식
        val emailPattern = "^[A-Za-z0-9.-]+@[A-Za-z.-]+.[A-Za-z]{2,}$"

        coreectEmail = email.matches(emailPattern.toRegex())
    }

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length > 0) {
            loginUser(email, password)
        } else {
            baseViewModel.baseViewModel.triggerToast("이메일과 비밀번호를 입력하세요.")
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        MyApplication.email = email
                        _isLoggedIn.value = true // 로그인 성공 시 이벤트 발생
                        baseViewModel.baseViewModel.setFireBaseUID()
                        baseViewModel.baseViewModel.triggerToast("로그인에 성공하였습니다.")
                    } else {
                        baseViewModel.baseViewModel.triggerVibration()
                        baseViewModel.baseViewModel.triggerToast("이메일 또는 비밀번호가 일치하지 않습니다.")
                    }
                }
        }
    }


}
