package com.maker.pacemaker.ui.activity.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.main.MainAlarmScreen
import com.maker.pacemaker.ui.screen.main.MainMenuScreen
import com.maker.pacemaker.ui.screen.main.MainMyPageScreen
import com.maker.pacemaker.ui.screen.main.MainScreen
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel

class SettingActivity : BaseActivity() {

    private lateinit var mainViewModel: MainBaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainBaseViewModel::class.java)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("dailyScreen") {
                }
            }
        }
    }

    // NavController 초기화 메서드 구현
    override fun initNavController(): NavHostController {
        return navController as NavHostController
    }

    override fun navigateToScreen(screenType: ScreenType) {

        val route = when (screenType) {
            ScreenType.DAILY -> "dailyScreen"
            ScreenType.RATIO -> "ratioScreen"
            ScreenType.CATEGORY -> "categoryScreen"
            ScreenType.LEVELTEST -> "levelTestScreen"
            else -> return
        }

        navController.navigate(route) {
            popUpTo(route) {
                inclusive = true // 포함하여 제거
            }
            launchSingleTop = true
        }
    }
}