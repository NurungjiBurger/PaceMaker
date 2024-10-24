package com.maker.pacemaker.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.MyApplication
import com.maker.pacemaker.data.model.ActivityNavigationTo
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenNavigationTo
import com.maker.pacemaker.data.model.ScreenType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    // 전역 Context 접근
    private val context: Context by lazy { MyApplication.getContext() }

    // SharedPreferences 접근
    //protected val sharedPreferences: SharedPreferences
   //     get() = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    // Editor 객체를 가져옵니다.
    //protected val editor = sharedPreferences.edit()

    //private val networkStatusTracker = NetworkStatusTracker(application)

    // 네트워크 상태를 관리하는 StateFlow
    //private val _networkStatus = MutableStateFlow(networkStatusTracker.networkStatus.value)
    //val networkStatus = _networkStatus.asStateFlow()

    init {
        viewModelScope.launch {
//            networkStatusTracker.networkStatus.collect { status ->
//                _networkStatus.value = !status
//            }
        }
    }

    // 서버 통신 관련 repository 변수 선언
    //protected val repository: UserRepository = UserRepository(
    //    RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)
    //)

    private val _userName = MutableStateFlow<String>("상빈")
    val userName: MutableStateFlow<String> get() = _userName

    // Activity navigation을 위한 LiveData
    private val _activityNavigationTo = MutableLiveData<ActivityNavigationTo>()
    val activityNavigationTo: LiveData<ActivityNavigationTo> get() = _activityNavigationTo

    // Screen navigation을 위한 LiveData
    private val _screenNavigationTo = MutableLiveData<ScreenNavigationTo>()
    val screenNavigationTo: LiveData<ScreenNavigationTo> get() = _screenNavigationTo


    // Activity로 이동
    fun goActivity(activity: ActivityType) {
        _activityNavigationTo.value = ActivityNavigationTo(activity)
    }

    // Screen으로 이동
    fun goScreen(screen: ScreenType) {
        _screenNavigationTo.value = ScreenNavigationTo(screen)
    }

    fun floatingToastMessage(message: String) {
    //    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}