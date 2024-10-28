package com.maker.pacemaker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : ComponentActivity() {

    @Inject
    lateinit var baseViewModel: BaseViewModel

    // lazy 초기화 방식으로 NavController 선언
    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// 상태바 색상을 검정색으로 설정
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)


        // ViewModel의 activity 전환 처리
        baseViewModel.activityNavigationTo.observe(this) { activityType ->
            activityType?.let {
                navigateToActivity(activityType.activityType)
            }
        }

        // ViewModel의 화면 전환 요청 처리
        baseViewModel.screenNavigationTo.observe(this) { navigationTo ->
            navigationTo?.screenType?.let { screenType ->
                navigateToScreen(screenType)
            }
        }
    }

    // 화면 전환을 처리하는 메서드
    protected open fun navigateToScreen(screenType: ScreenType) {
        // 상속받은 클래스에서 구현 가능하도록
    }

    protected open fun navigateToActivity(activityType: ActivityType) {
        // 상속받은 클래스에서 구현 가능하도록
    }
}