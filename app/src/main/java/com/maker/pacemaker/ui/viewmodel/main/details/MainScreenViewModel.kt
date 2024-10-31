package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.data.model.remote.loginRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base
    val repository = baseViewModel.baseViewModel.repository
    private val _registrationResult = MutableLiveData<String?>()
    val registrationResult: MutableLiveData<String?> get() = _registrationResult

    fun identifyUserByToken() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.e("identifyUserByToken", "사용자가 로그인되지 않았습니다.")
            return
        }

        user.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                val UID = user.uid
                val req = loginRequest(idToken.toString(), UID)

                viewModelScope.launch {
                    try {
                        val loginRes = repository.sendIdToken(usertoken = req)
                        // loginRes 처리 로직
                        Log.d("LoginResponse", "서버 응답: $loginRes")
                    } catch (e: Exception) {
                        Log.e("sendIdTokenError", "ID 토큰 전송 중 오류 발생: ${e.message}")
                    }
                }
            } else {
                Log.e("IDTokenError", "ID 토큰을 가져오는 데 실패했습니다: ${task.exception?.message}")
            }
        }
    }

}
