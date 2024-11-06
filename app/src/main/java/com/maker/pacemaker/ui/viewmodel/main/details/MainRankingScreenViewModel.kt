package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.User
import com.maker.pacemaker.data.model.remote.SearchUser
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainRankingScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base

    val repository = baseViewModel.repository

    // 검색할 유저 이름
    private val _userName = MutableStateFlow("")
    val userName = _userName

    // 검색된 유저들
    private val _userList =  MutableStateFlow<List<SearchUser>>(emptyList()) // MutableStateFlow<List<User>>(emptyList())
    val userList = _userList

    fun restate() {
        _userList.value = emptyList()
        _userName.value = ""
        fetchUsers(_userName.value)
    }

    private fun fetchUsers(searchKeyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.searchUser(searchKeyword)

            if (response.users.isNotEmpty()) {
                _userList.value = response.users
            } else {
                _userList.value = emptyList()
            }
        }
    }

    fun onSearchButtonClicked() {
        // userName에 저장된 검색어로 필터링
        fetchUsers(_userName.value)
    }

    fun onUserNameChanged(userName: String) {
        _userName.value = userName
    }

    fun toggleFollow(user: User) {
//        _userList.value = _userList.value.map {
//            if (it.name == user.name) it.copy(isFollowing = !it.isFollowing) else it
//        }
//        // 업데이트된 팔로우 상태에 따라 토스트 메시지 표시
//        baseViewModel.baseViewModel.triggerToast("${user.name} is now ${if (user.isFollowing) "followed" else "unfollowed"}")
    }

}