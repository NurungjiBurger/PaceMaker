package com.maker.pacemaker.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
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
import com.maker.pacemaker.data.model.remote.ApiService
import com.maker.pacemaker.data.model.remote.RetrofitClient
import com.maker.pacemaker.data.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
) : ViewModel() {

    // 전역 Context 접근
    val context: Context by lazy { MyApplication.getContext() }

    // SharedPreferences 접근
    val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    // Editor 객체를 가져옵니다.
    val editor = sharedPreferences.edit()

    // 휴대폰 진동
    @SuppressLint("ServiceCast")
    fun triggerVibration() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            val vibrationEffect =
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }

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
    val repository: UserRepository = UserRepository(
        RetrofitClient.getRetrofitInstance(context).create(ApiService::class.java)
    )

    val _userName = MutableStateFlow<String>("상빈")
    val userName: MutableStateFlow<String> get() = _userName

    // Activity navigation을 위한 LiveData
    val _activityNavigationTo = MutableLiveData<ActivityNavigationTo?>()
    val activityNavigationTo: MutableLiveData<ActivityNavigationTo?> get() = _activityNavigationTo

    // Screen navigation을 위한 LiveData
    val _screenNavigationTo = MutableLiveData<ScreenNavigationTo?>()
    val screenNavigationTo: MutableLiveData<ScreenNavigationTo?> get() = _screenNavigationTo

    var _previousScreen: ScreenType? = null
    var _previousActivity: ActivityType? = null

    val previousScreen: ScreenType?
        get() = _previousScreen

    val previousActivity: ActivityType?
        get() = _previousActivity

    fun restate() {
        _previousScreen = null
        _previousActivity = null

        _activityNavigationTo.postValue(null)
        _screenNavigationTo.postValue(null)
    }


    // Activity로 이동
    fun goActivity(activity: ActivityType) {

        Log.d("BaseViewModel", "goActivity: $activity")

        _previousActivity = activityNavigationTo.value?.activityType?: ActivityType.MAIN
        _previousScreen = ScreenType.FINISH
        _activityNavigationTo.value = ActivityNavigationTo(activity)
    }

    // Screen으로 이동
    fun goScreen(screen: ScreenType) {

        Log.d("BaseViewModel", "goScreen: $screen")
        Log.d("BaseViewModel", "previous Screen: $_previousScreen")

        _previousScreen = screenNavigationTo.value?.screenType?: ScreenType.MAIN
        _previousActivity = ActivityType.FINISH
        _screenNavigationTo.value = ScreenNavigationTo(screen)

        Log.d("BaseViewModel", "previous Screen: $_previousScreen")
    }

    fun triggerToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}