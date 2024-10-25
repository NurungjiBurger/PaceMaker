package com.maker.pacemaker.ui.activity.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.setting.SettingDailyScreen
import com.maker.pacemaker.ui.screen.setting.SettingMyPageScreen
import com.maker.pacemaker.ui.screen.setting.SettingRatioScreen
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingDailyScreenViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingMyPageScreenViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingRatioScreenViewModel

class SettingActivity : BaseActivity() {

    private val settingMyPageScreenViewModel: SettingMyPageScreenViewModel by viewModels()
    private val settingDailyScreenViewModel: SettingDailyScreenViewModel by viewModels()
    private val settingRatioScreenViewModel: SettingRatioScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "myPageScreen") {
                composable("dailyScreen") { SettingDailyScreen(settingDailyScreenViewModel) }
                composable("myPageScreen") { SettingMyPageScreen(settingMyPageScreenViewModel)}
                composable("ratioScreen") { SettingRatioScreen(settingRatioScreenViewModel)}
            }
        }
    }


    // NavController 초기화 메서드 구현
    override fun initNavController(): NavHostController {
        return navController as NavHostController
    }

    override fun navigateToScreen(screenType: ScreenType) {

        val route = when (screenType) {
            ScreenType.MYPAGE -> "mypageScreen"
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