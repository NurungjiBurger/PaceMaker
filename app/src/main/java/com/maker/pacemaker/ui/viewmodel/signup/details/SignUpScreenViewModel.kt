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
) : ViewModel() {

    val baseViewModel = base
    var passWordSettingEnabled = false

    val auth = baseViewModel.baseViewModel.auth

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

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
                                baseViewModel.baseViewModel.triggerToast("전송된 메일을 확인해 주세요.")

                                baseViewModel.baseViewModel.setFireBaseUID()

                                _registrationResult.value = "전송된 메일을 확인해 주세요."
                                // 이메일 인증 확인 절차 시작
                                checkIfEmailVerifiedPeriodically()
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

    // 주기적으로 이메일 인증 상태를 확인하는 함수
    private fun checkIfEmailVerifiedPeriodically() {
        val user = auth.currentUser
        val handler = android.os.Handler()
        val checkInterval = 3000L // 3초마다 체크

        val verificationCheck = object : Runnable {
            override fun run() {
                user?.reload()?.addOnCompleteListener { task ->
                    if (task.isSuccessful && user.isEmailVerified) {
                        // 이메일 인증이 완료된 경우 Firebase에 최종 사용자 데이터 저장
                        saveUserDataToDatabase()
                        baseViewModel.baseViewModel.triggerToast("이메일 인증 완료. 회원가입에 성공하였습니다.")
                        _registrationResult.value = "이메일 인증 완료."
                        handler.removeCallbacks(this) // 반복 중지
                    } else {
                        // 이메일 인증이 완료되지 않은 경우 일정 시간 후 다시 확인
                        handler.postDelayed(this, checkInterval)
                    }
                }
            }
        }

        handler.post(verificationCheck)
    }

    // Firebase Firestore 또는 Realtime Database에 사용자 정보 저장
    private fun saveUserDataToDatabase() {
        val user = auth.currentUser
        val uid = user?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        val userData = hashMapOf(
            "uid" to uid,
            "email" to user.email,
            "isVerified" to true // 인증이 완료된 사용자로 표시
        )

        db.collection("users").document(uid)
            .set(userData)
            .addOnSuccessListener {
                baseViewModel.baseViewModel.triggerToast("사용자 정보가 등록되었습니다.")
            }
            .addOnFailureListener { e ->
                baseViewModel.baseViewModel.triggerToast("사용자 정보 등록 실패: ${e.message}")
            }
    }
}
