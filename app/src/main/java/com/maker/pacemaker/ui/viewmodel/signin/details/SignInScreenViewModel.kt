package com.maker.pacemaker.ui.viewmodel.signin.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.MyApplication
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SignInScreenViewModel @Inject constructor() : SignInBaseViewModel() {

    private var _errorMessage: String? = null
    val errorMessage: String?
        get() = _errorMessage

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            loginUser(email, password)
        } else {
            _errorMessage = "이메일과 비밀번호를 입력하세요."
        }
    }

    private fun loginUser(email: String, password: String) {
        // Coroutine을 사용하여 비동기 작업 수행
        viewModelScope.launch {
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        MyApplication.email = email
                        // 로그인 성공 시 필요한 동작 수행
                    } else {
                        _errorMessage = "로그인 실패: ${task.exception?.message}"
                    }
                }
        }
    }
}
