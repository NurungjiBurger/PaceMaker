package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.data.model.remote.loginRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
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
        user?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) { // 알아둬
                    val idToken = task.result?.token
                    val UID = user.uid
                    if (idToken != null) {
                        // 서버로 전송
                        viewModelScope.launch {
                            sendIdTokenToServer(idToken, UID)
                        }
                    } else {
                        _registrationResult.value = "ID 토큰이 비어 있습니다."
                    }
                } else {
                    _registrationResult.value = "ID 토큰을 가져오는 데 실패했습니다."
                    Log.e("IDTokenError", "ID 토큰을 가져오는 데 실패했습니다: ${task.exception?.message}")
                }
            }
    }

    private suspend fun sendIdTokenToServer(idToken: String, UID: String) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //val idToken = task.result?.token
                // ID 토큰 사용 (예: 서버에 전송하여 검증 등)
                //val request = loginRequest(idToken,UID)
                viewModelScope.launch(Dispatchers.IO) {
                    val response = repository.sendIdToken(idToken, UID)
                    Log.d("FirebaseIDToken", "ID Token: $idToken")
                    Log.d("FirebaseIDToken", "UID: $UID")
                    Log.d("FirebaseIDToken", "response: $response")
                }

            } else {
                // ID 토큰을 가져오는 데 실패한 경우
                Log.e("FirebaseIDToken", "Failed to retrieve ID Token", task.exception)
            }
        }

    }




    }
