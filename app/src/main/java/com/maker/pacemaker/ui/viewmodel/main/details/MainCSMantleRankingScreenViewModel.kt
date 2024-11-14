package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.SimilarityWord
import com.maker.pacemaker.data.model.remote.SolvedUser
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    // 필요한 정보만 저장하는 myUserInfo StateFlow
    private val _myUserInfo = MutableStateFlow<UserInfo?>(null)
    val myUserInfo: StateFlow<UserInfo?> get() = _myUserInfo

    init {
        showRanking()
    }

    fun showRanking() {
        viewModelScope.launch {
            val response = repository.getSolvedUsers()

            Log.d("MainCSMantleScreenViewModel", "Response: $response")

            // 새로운 순위 리스트를 생성하고 내림차순으로 정렬
            val newRank = response.map { user ->
                SolvedUser(user.uid, user.nickname, user.try_cnt)
            }.sortedBy { it.try_cnt }

            // _userRanks 업데이트
            _userRanks.value = newRank

            Log.d("MainCSMantleScreenViewModel", "New Rank: $newRank")

            // 자신의 uid와 일치하는 사용자 정보 찾기
            val myUid = baseViewModel.sharedPreferences.getString("fireBaseUID", "")
            val myIndex = newRank.indexOfFirst { it.uid == myUid }

            Log.d("MainCSMantleScreenViewModel", "My UID: $myUid, My Index: $myIndex")

            if (myIndex != -1) {
                val myUser = newRank[myIndex]
                _myUserInfo.value = UserInfo(index = myIndex, try_cnt = myUser.try_cnt, nickname = myUser.nickname)

                Log.d("MainCSMantleScreenViewModel", "My Info - Index: $myIndex, Try_cnt: ${myUser.try_cnt}, Nickname: ${myUser.nickname}")
            } else {
                Log.d("MainCSMantleScreenViewModel", "My Info not found in the ranking list")
            }
        }
    }

}

// 필요한 정보만 포함하는 데이터 클래스
data class UserInfo(
    val index: Int,
    val try_cnt: Int,
    val nickname: String
)