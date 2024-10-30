package com.maker.pacemaker.ui.viewmodel.signup.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@HiltViewModel
open class SignUpScreenViewModel @Inject constructor(
    private val base: SignUpBaseViewModel,
    private val auth: FirebaseAuth
) : ViewModel() {

    val baseViewModel = base
    var passWordSettingEnabled = false
    private val db = FirebaseFirestore.getInstance()

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    fun checkEmail(email: String) {
        // 이메일 형식을 확인하는 정규 표현식
        val emailPattern = "^[A-Za-z0-9.-]+@[A-Za-z.-]+.[A-Za-z]{2,}$"

        passWordSettingEnabled = email.matches(emailPattern.toRegex())
    }

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            registerUser(email, password)
        } else {
            baseViewModel.baseViewModel.triggerToast("이메일과 비밀번호를 입력하세요.")
        }
    }

    fun enrollUserToServer(nickName: String) {
        // 서버에 유저 등록하기
        // firebase의 uid와 닉네임을 등록해주면 된다.
        baseViewModel.baseViewModel.goActivity(ActivityType.MAIN)
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { sendTask ->
                            if (sendTask.isSuccessful) {
                                _registrationResult.value = "이메일 인증을 완료해 주세요."
                                checkIfEmailVerifiedPeriodically()
                            } else {
                                _registrationResult.value = "메일 전송 실패"
                            }
                        }
                } else {
                    _registrationResult.value = "회원가입 실패"
                }
            }
    }

    // 이메일 인증 상태 확인
    private fun checkIfEmailVerifiedPeriodically() {
        val user = auth.currentUser
        val handler = android.os.Handler()
        val checkInterval = 3000L

        val verificationCheck = object : Runnable {
            override fun run() {
                user?.reload()?.addOnCompleteListener { task ->
                    if (task.isSuccessful && user.isEmailVerified) {
                        saveUserToDatabase(user.uid,user.email)
                        _registrationResult.value = "회원가입이 완료되었습니다."
                        handler.removeCallbacks(this)
                    } else {
                        handler.postDelayed(this, checkInterval)
                    }
                }
            }
        }
        handler.post(verificationCheck)
    }

    // 이메일 인증된 사용자만 Firestore에 최종 등록
    private fun saveUserToDatabase(uid: String, email: String?) {
        val userData = hashMapOf(
            "uid" to uid,
            "email" to email,
            "isVerified" to true
        )
        db.collection("users").document(uid).set(userData)
            .addOnSuccessListener {
                baseViewModel.baseViewModel.triggerToast("인증된 사용자 등록 성공")
            }
            .addOnFailureListener { e ->
                baseViewModel.baseViewModel.triggerToast("등록 실패: ${e.message}")
            }
    }
}
