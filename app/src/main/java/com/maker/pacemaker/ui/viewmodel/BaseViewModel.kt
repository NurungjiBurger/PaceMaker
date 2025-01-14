package com.maker.pacemaker.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.MyApplication
import com.maker.pacemaker.data.model.ActivityNavigationTo
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenNavigationTo
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.remote.ApiService
import com.maker.pacemaker.data.model.remote.RetrofitClient
import com.maker.pacemaker.data.model.remote.User
import com.maker.pacemaker.data.model.remote.sendFcmToken
import com.maker.pacemaker.data.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    val auth: FirebaseAuth
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> get() = _isLoading

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

    private val _userName = MutableStateFlow<String>("상빈")
    val userName: MutableStateFlow<String> get() = _userName

    val _dailyCount = MutableStateFlow(0)
    val dailyCount: MutableStateFlow<Int> get() = _dailyCount

    // Activity navigation을 위한 LiveData
    val _activityNavigationTo = MutableLiveData<ActivityNavigationTo?>()
    val activityNavigationTo: MutableLiveData<ActivityNavigationTo?> get() = _activityNavigationTo

    // Screen navigation을 위한 LiveData
    val _screenNavigationTo = MutableLiveData<ScreenNavigationTo?>()
    val screenNavigationTo: MutableLiveData<ScreenNavigationTo?> get() = _screenNavigationTo

    var activityChange: Boolean = false

    private val _userInfo = MutableStateFlow<User>(User("","",0,0,0,emptyList(),0))
    val userInfo: StateFlow<User> get() = _userInfo

    // allQuizSolved와 CSMantleSolved 상태를 관리하는 StateFlow
    private val _allQuizSolved = MutableStateFlow(loadAllQuizSolved())
    val allQuizSolved: StateFlow<Boolean> get() = _allQuizSolved

    private val _CSMantleSolved = MutableStateFlow(loadCSMantleSolved())
    val CSMantleSolved: StateFlow<Boolean> get() = _CSMantleSolved

    // allQuizSolved 값을 SharedPreferences에서 불러오는 함수
    private fun loadAllQuizSolved(): Boolean {
        return sharedPreferences.getBoolean("allQuizSolved", false)
    }

    // CSMantleSolved 값을 SharedPreferences에서 불러오는 함수
    private fun loadCSMantleSolved(): Boolean {
        return sharedPreferences.getBoolean("CSMantleSolved", false)
    }

    // allQuizSolved 값을 변경하고 SharedPreferences에 저장하는 함수
    fun setAllQuizSolved(value: Boolean) {
        _allQuizSolved.value = value
        editor.putBoolean("allQuizSolved", value).apply()
    }

    // CSMantleSolved 값을 변경하고 SharedPreferences에 저장하는 함수
    fun setCSMantleSolved(value: Boolean) {
        _CSMantleSolved.value = value
        sharedPreferences.edit().putBoolean("CSMantleSolved", value).apply()
    }



    fun setLoading(isLoading: Boolean) {
        Log.d("isLoading", _isLoading.value.toString())
        _isLoading.value = isLoading
        Log.d("isLoading", _isLoading.value.toString())
    }

    fun getUserInfo() {
        viewModelScope.launch {
            _userInfo.value = repository.getMyUserInfo()
            _userInfo.value?.let { userInfo ->
                Log.d("UserInfo", userInfo.toString())
                saveUserInfoToPreferences(userInfo)
            }

            // fcmToken 서버 테스트
            sendFCMTokenToServer(sharedPreferences.getString("fireBaseUID", "")!!, sharedPreferences.getString("fcmToken", "")!!)
//            getFCMToken()
//            deleteFCMToekn(sharedPreferences.getString("fcmToken", "")!!)
        }
    }

    private fun saveUserInfoToPreferences(userInfo: User) {
        with(editor) {
            putString("fireBaseUID", userInfo.uid)
            putString("nickname", userInfo.nickname)
            putInt("exp", userInfo.exp)
            putInt("level", userInfo.level)
            putInt("myDailyCount", userInfo.daily_cnt)

            // List<Int> -> Set<String>으로 변환하여 저장
            val categoriesStringSet = userInfo.preferred_categories.map { it.toString() }.toSet()
            putStringSet("preferred_categories", categoriesStringSet)

            putInt("followers_count", userInfo.followers_count)


            val currentDate = getCurrentDate()
            val savedDate = sharedPreferences.getString("date", "")

            if (currentDate != savedDate) {
                putInt("problemIndex", 0)
                putInt("todaySolvedCount", 0)
                putInt("todayWrongCount", 0)

                setAllQuizSolved(false)
                setCSMantleSolved(false)
            }

            apply()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun restate() {
        _activityNavigationTo.postValue(null)
        _screenNavigationTo.postValue(null)
    }

    // Activity로 이동
    fun goActivity(activity: ActivityType) {
        _activityNavigationTo.value = ActivityNavigationTo(activity)
    }

    // Screen으로 이동
    fun goScreen(screen: ScreenType) {
        _screenNavigationTo.value = ScreenNavigationTo(screen)
    }

    // time 설정 안하면 2초동안 화면에 표시
    fun triggerToast(message: String, time: Long = 2000) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        }, time)
    }

    fun sendFCMTokenToServer(userId: String, fcmToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = sendFcmToken(userId, fcmToken, "Android")
            val response = try {
                repository.sendFcmToken(request)
            } catch (e: Exception) {
                Log.e("FCM", "sendFCMTokenToServer: $e")
                null
            }
        }
    }

    fun getFCMToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                repository.getFcmToken(sharedPreferences.getString("fireBaseUID", "")!!)
            } catch (e: Exception) {
                Log.e("FCM", "getFCMToken: $e")
                null
            }
        }
    }

    fun deleteFCMToekn(toekn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                repository.deleteFcmToken(toekn)
            } catch (e: Exception) {
                Log.e("FCM", "deleteFCMToekn: $e")
            }

            Log.d("FCM", "deleteFCMToekn: $response")
        }
    }

    fun setFireBaseUID() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            // UID를 성공적으로 가져왔을 때 처리
            Log.d("FirebaseUID", "User UID: $uid")
            editor.putString("fireBaseUID", uid)
            editor.apply()

            if (sharedPreferences.getString("fcmtoken", "") != "") {
                sendFCMTokenToServer(uid, sharedPreferences.getString("fcmToken", "")!!)
            }
        } else {
            // UID를 가져오지 못한 경우 (예: 로그아웃 상태)
            Log.d("FirebaseUID", "User is not logged in.")
        }
    }
}