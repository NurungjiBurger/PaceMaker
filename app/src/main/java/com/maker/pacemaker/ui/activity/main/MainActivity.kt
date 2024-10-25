package com.maker.pacemaker.ui.activity.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.main.MainAlarmScreen
import com.maker.pacemaker.ui.screen.main.MainMenuScreen
import com.maker.pacemaker.ui.screen.main.MainScreen
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMenuScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {


    private val mainViewModel: MainBaseViewModel by viewModels()
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val mainAlarmScreenViewModel: MainAlarmScreenViewModel by viewModels()
    private val mainMenuScreenViewModel: MainMenuScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("mainScreen") { MainScreen(mainScreenViewModel) }
                composable("alarmScreen") { MainAlarmScreen(mainAlarmScreenViewModel) }
                composable("menuScreen") { MainMenuScreen(mainMenuScreenViewModel) }
            }
        }
    }

    // NavController 초기화 메서드 구현
    override fun initNavController(): NavHostController {
        return navController as NavHostController
    }

    override fun navigateToActivity(activityType: ActivityType) {
        val intent = activityType.intentCreator(this)
        if (activityType == ActivityType.FINISH) {
            finish() // 현재 Activity 종료
        } else if (intent != null) {
            startActivity(intent) // Intent가 null이 아닐 때만 Activity 시작
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {

        val route = when (screenType) {
            ScreenType.MAIN -> "mainscreen"
            ScreenType.ALARM -> "alarmscreen"
            ScreenType.MENU -> "menuscreen"
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