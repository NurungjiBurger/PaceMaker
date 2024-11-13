package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.SimilarityWord
import com.maker.pacemaker.data.model.remote.SolvedUser
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainCSMantleRankingScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base

    val repository = baseViewModel.repository

    private val _userRanks = MutableStateFlow<List<SolvedUser>>(emptyList())
    val userRanks = _userRanks

    fun showRanking() {
        viewModelScope.launch {
            val response = repository.getSolvedUsers()

            Log.d("MainCSMantleScreenViewModel", "Response: $response")

            // newRank 초기화
            var newRank = emptyList<SolvedUser>()

            response.forEach { user ->
                Log.d("MainCSMantleScreenViewModel", "Try_cnt: ${user.try_cnt}, Nickname: ${user.nickname}, UID: ${user.uid}")
                val try_cnt = user.try_cnt
                val name = user.nickname
                val userid = user.uid

                // newRank에 사용자 추가
                newRank = newRank + SolvedUser(userid, name, try_cnt)
            }

            // 내림차순으로 정렬 후 _userRanks에 업데이트
            _userRanks.value = newRank.sortedByDescending { it.try_cnt }
        }
    }
}