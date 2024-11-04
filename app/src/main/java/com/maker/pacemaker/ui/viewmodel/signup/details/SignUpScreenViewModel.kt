package com.maker.pacemaker.ui.viewmodel.signup.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maker.pacemaker.MyApplication
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


@HiltViewModel
open class SignUpScreenViewModel @Inject constructor(
    private val base: SignUpBaseViewModel,
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val signUpViewModel = base


    var passWordSettingEnabled = false
    val repository = baseViewModel.repository

    val auth = baseViewModel.auth

    private val _registrationResult = MutableLiveData<String>()
    private val _isLoggedIn = MutableLiveData<Boolean>() // 로그인 상태 LiveData
    val registrationResult: LiveData<String> get() = _registrationResult

    fun enrollUserToServer(email: String, password: String, nickName: String) {
        // 서버에 유저 등록하기
        // firebase의 uid와 닉네임을 등록해주면 된다.
        CoroutineScope(Dispatchers.IO).launch {
            // 로그인 처리
            loginUser(email, password)

            // 사용자 생성 요청
            val createuserResponse = repository.createUser(nickName)
            Log.d("SignUpScreenViewModel", "createUserResponse: $createuserResponse")

            // UI 업데이트는 메인 스레드에서 해야 하므로 withContext를 사용
            withContext(Dispatchers.Main) {
                // 유저 등록 후 메인 화면으로 이동
                baseViewModel.goActivity(ActivityType.MAIN)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        MyApplication.email = email
                        _isLoggedIn.value = true // 로그인 성공 시 이벤트 발생
                        baseViewModel.setFireBaseUID()
                        baseViewModel.triggerToast("로그인에 성공하였습니다.")
                    } else {
                        baseViewModel.triggerVibration()
                        baseViewModel.triggerToast("이메일 또는 비밀번호가 일치하지 않습니다.")
                    }
                }
        }
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
            baseViewModel.triggerToast("이메일과 비밀번호를 입력하세요.")
        }
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { sendTask ->
                            if (sendTask.isSuccessful) {
                                baseViewModel.triggerToast("전송된 메일을 확인해 주세요.")

                                baseViewModel.setFireBaseUID()

                                _registrationResult.value = "전송된 메일을 확인해 주세요."
                                // 이메일 인증 확인 절차 시작
                                checkIfEmailVerifiedPeriodically()
                            } else {
                                baseViewModel.triggerToast("메일 전송 실패")
                                _registrationResult.value = "메일 전송 실패"
                            }
                        }
                } else {
                    baseViewModel.triggerToast("회원가입 실패")
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
                        baseViewModel.triggerToast("이메일 인증 완료. 회원가입에 성공하였습니다.")
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
                baseViewModel.triggerToast("사용자 정보가 등록되었습니다.")
            }
            .addOnFailureListener { e ->
                baseViewModel.triggerToast("사용자 정보 등록 실패: ${e.message}")
            }
    }
}