package com.maker.pacemaker.ui.activity.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.main.MainAlarmScreen
import com.maker.pacemaker.ui.screen.main.MainMenuScreen
import com.maker.pacemaker.ui.screen.main.MainMyPageScreen
import com.maker.pacemaker.ui.screen.main.MainScreen
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMenuScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMyPageScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {


    private lateinit var mainViewModel: MainBaseViewModel

    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val mainAlarmScreenViewModel: MainAlarmScreenViewModel by viewModels()
    private val mainMenuScreenViewModel: MainMenuScreenViewModel by viewModels()
    private val mainMyPageScreenViewModel: MainMyPageScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainBaseViewModel::class.java)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("mainScreen") { MainScreen(baseViewModel, mainViewModel, mainScreenViewModel) }
                composable("alarmScreen") { MainAlarmScreen(baseViewModel, mainViewModel, mainAlarmScreenViewModel) }
                composable("menuScreen") { MainMenuScreen(baseViewModel, mainViewModel, mainMenuScreenViewModel) }
                composable("myPageScreen") { MainMyPageScreen(baseViewModel, mainViewModel, mainMyPageScreenViewModel) }
            }
        }
    }

    // NavController 초기화 메서드 구현
    override fun initNavController(): NavHostController {
        return navController as NavHostController
    }

    override fun navigateToScreen(screenType: ScreenType) {

        val route = when (screenType) {
            ScreenType.MAIN -> "mainscreen"
            ScreenType.ALARM -> "alarmscreen"
            ScreenType.MENU -> "menuscreen"
            ScreenType.MYPAGE -> "mypagescreen"
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