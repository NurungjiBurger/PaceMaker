package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainCSMantleRankingScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base

    val repository = baseViewModel.repository

    fun showRanking() {
        viewModelScope.launch {
            val response = repository.getSolvedUsers()

            Log.d("MainCSMantleScreenViewModel", "Response: $response")
            response.forEach { user ->
                Log.d("MainCSMantleScreenViewModel", "Try_cnt: ${user.try_cnt}, Nickname: ${user.nickname}, UID: ${user.uid}")
            }

        }
    }

}