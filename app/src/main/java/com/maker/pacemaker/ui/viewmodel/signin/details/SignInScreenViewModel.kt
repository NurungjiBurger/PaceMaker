package com.maker.pacemaker.ui.viewmodel.signin.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isLoggedIn = MutableLiveData<Boolean>() // 로그인 상태 LiveData
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            loginUser(email, password)
        } else {
            _errorMessage = "이메일과 비밀번호를 입력하세요."
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        MyApplication.email = email
                        _isLoggedIn.value = true // 로그인 성공 시 이벤트 발생
                    } else {
                        _errorMessage = "로그인 실패: ${task.exception?.message}"
                    }
                }
        }
    }
}
