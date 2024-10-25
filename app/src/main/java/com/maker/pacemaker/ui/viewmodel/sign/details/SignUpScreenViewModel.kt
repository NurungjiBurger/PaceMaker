package com.maker.pacemaker.ui.viewmodel.sign.details

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.sign.SignBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


@HiltViewModel
open class SignUpScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : SignBaseViewModel() {

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    fun checkUser(email: String, password: String) {
        if (email.isNotEmpty() && password.length >= 6) {
            // 회원가입 코드
            registerUser(email, password)
        } else {
            //Toast.makeText(context, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { sendTask ->
                            if (sendTask.isSuccessful) {
                                _registrationResult.value = "회원가입에 성공하였습니다. 전송된 메일을 확인해 주세요."
                            } else {
                                _registrationResult.value = "메일 전송 실패"
                            }
                        }
                } else {
                    _registrationResult.value = "회원가입 실패"
                }
            }
    }
}
