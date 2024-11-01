package com.maker.pacemaker.ui.viewmodel.main.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class MainScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base

    init {
        val user = baseViewModel.baseViewModel.auth.currentUser

        if (user == null) {
            Log.e("identifyUserByToken", "사용자가 로그인되지 않았습니다.")
        } else {
            user.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        baseViewModel.baseViewModel.editor.putString("idToken", idToken).apply()

                        Log.d("idtoken", "사용자의 토큰: $idToken")              
                        baseViewModel.baseViewModel.getUserInfo()

                    } else {
                        Log.e("identifyUserByToken", "토큰 가져오기 실패: ${task.exception}")
                    }
                }
        }
    }


}