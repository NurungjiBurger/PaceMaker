package com.maker.pacemaker.ui.viewmodel.signup.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


@HiltViewModel
open class SignUpScreenViewModel @Inject constructor(
    private val base: SignUpBaseViewModel,
) : ViewModel() {

    val baseViewModel = base

    val auth = baseViewModel.baseViewModel.auth

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    var passWordSettingEnabled = false

    fun enrollUserToServer(nickName: String) {
        // 서버에 유저 등록하기
        // firebase의 uid와 닉네임을 등록해주면 된다.
        baseViewModel.baseViewModel.goActivity(ActivityType.MAIN)
    }

    fun checkEmail(email: String) {
        // 이메일 형식을 확인하는 정규 표현식
        val emailPattern = "^[A-Za-z0-9.-]+@[A-Za-z.-]+.[A-Za-z]{2,}$"

        passWordSettingEnabled = email.matches(emailPattern.toRegex())
    }

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            // 회원가입 코드
            registerUser(email, password)
        } else {
            baseViewModel.baseViewModel.triggerToast("이메일과 비밀번호를 입력하세요.")
        }
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { sendTask ->
                            if (sendTask.isSuccessful) {
                                baseViewModel.baseViewModel.triggerToast("회원가입에 성공하였습니다. 전송된 메일을 확인해 주세요.")
                                baseViewModel.baseViewModel.setFireBaseUID()
                                _registrationResult.value = "회원가입에 성공하였습니다. 전송된 메일을 확인해 주세요."
                            } else {
                                baseViewModel.baseViewModel.triggerToast("메일 전송 실패")
                                _registrationResult.value = "메일 전송 실패"
                            }
                        }
                } else {
                    baseViewModel.baseViewModel.triggerToast("회원가입 실패")
                    _registrationResult.value = "회원가입 실패"
                }
            }
    }
}
