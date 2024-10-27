package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.data.model.User
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class MainRankingScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base

    // 검색할 유저 이름
    private val _userName = MutableStateFlow("")
    val userName = _userName

    // 검색된 유저들
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList

    init {
        // 초기화
        _userName.value = ""
        _userList.value = listOf(
            User(name = "김철수", level = 2, followers = 500, isFollowing = true),
            User(name = "이영희", level = 4, followers = 2500, isFollowing = false),
            User(name = "박지수", level = 1, followers = 120, isFollowing = true),
            User(name = "최민준", level = 3, followers = 8800, isFollowing = false),
            User(name = "정다은", level = 0, followers = 40, isFollowing = true),
            User(name = "김철수", level = 2, followers = 500, isFollowing = true),
            User(name = "이영희", level = 4, followers = 2500, isFollowing = false),
            User(name = "박지수", level = 1, followers = 120, isFollowing = true),
            User(name = "최민준", level = 3, followers = 8800, isFollowing = false),
            User(name = "김철수", level = 2, followers = 500, isFollowing = true),
            User(name = "이영희", level = 4, followers = 2500, isFollowing = false),
            User(name = "박지수", level = 1, followers = 120, isFollowing = true),
            User(name = "최민준", level = 3, followers = 8800, isFollowing = false),
        )
    }

    fun onUserNameChanged(userName: String) {
        _userName.value = userName
    }

    fun toggleFollow(user: User) {
        _userList.value = _userList.value.map {
            if (it.name == user.name) it.copy(isFollowing = !it.isFollowing) else it
        }
        // 업데이트된 팔로우 상태에 따라 토스트 메시지 표시
        baseViewModel.baseViewModel.triggerToast("${user.name} is now ${if (user.isFollowing) "followed" else "unfollowed"}")
    }

}