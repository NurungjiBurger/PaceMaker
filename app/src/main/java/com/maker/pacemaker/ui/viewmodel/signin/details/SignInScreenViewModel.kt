package com.maker.pacemaker.ui.viewmodel.signin.details

import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SignInScreenViewModel @Inject constructor() : SignInBaseViewModel() {

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            // 회원가입 코드
            //registerUser(email, password)
        } else {
            //Toast.makeText(context, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
