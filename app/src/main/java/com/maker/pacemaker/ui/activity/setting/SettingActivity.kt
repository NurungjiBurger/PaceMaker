package com.maker.pacemaker.ui.activity.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.setting.SettingCategoryScreen
import com.maker.pacemaker.ui.screen.setting.SettingDailyScreen
import com.maker.pacemaker.ui.screen.setting.SettingMyPageScreen
import com.maker.pacemaker.ui.screen.setting.SettingRatioScreen
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingCategoryScreenViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingDailyScreenViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingMyPageScreenViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingRatioScreenViewModel

class SettingActivity : BaseActivity() {

    private val settingMyPageScreenViewModel: SettingMyPageScreenViewModel by viewModels()
    private val settingDailyScreenViewModel: SettingDailyScreenViewModel by viewModels()
    private val settingRatioScreenViewModel: SettingRatioScreenViewModel by viewModels()
    private val settingCategoryScreenViewModel: SettingCategoryScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "myPageScreen") {
                composable("dailyScreen") { SettingDailyScreen(settingDailyScreenViewModel) }
                composable("myPageScreen") { SettingMyPageScreen(settingMyPageScreenViewModel)}
                composable("ratioScreen") { SettingRatioScreen(settingRatioScreenViewModel)}
                composable("categoryScreen") { SettingCategoryScreen(settingCategoryScreenViewModel)}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        baseViewModel.restate()
    }


    override fun navigateToScreen(screenType: ScreenType) {

        val route = when (screenType) {
            ScreenType.MYPAGE -> "myPageScreen"
            ScreenType.DAILY -> "dailyScreen"
            ScreenType.RATIO -> "ratioScreen"
            ScreenType.CATEGORY -> "categoryScreen"
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