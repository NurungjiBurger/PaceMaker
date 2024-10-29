package com.maker.pacemaker.ui.activity.boot

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.viewmodel.boot.BootBaseViewModel
import com.maker.pacemaker.ui.viewmodel.boot.details.BootScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import com.maker.pacemaker.ui.viewmodel.boot.details.EntryScreenViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.boot.BootScreen
import com.maker.pacemaker.ui.screen.boot.EntryScreen

@AndroidEntryPoint
class BootActivity : BaseActivity() {

    private val bootScreenViewModel: BootScreenViewModel by viewModels()
    private val entryScreenViewModel: EntryScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            baseViewModel._previousActivity = ActivityType.BOOT

            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("mainScreen") { BootScreen(bootScreenViewModel) }
                composable("entryScreen") { EntryScreen(entryScreenViewModel) }
            }
        }
    }

    override fun navigateToActivity(activityType: ActivityType) {

        Log.d("MainActivity", "navigateToActivity: $activityType")

        if (baseViewModel.previousActivity != ActivityType.BOOT) return

        val intent = activityType.intentCreator(this)
        if (activityType == ActivityType.FINISH) {
            finish() // 현재 Activity 종료
        } else if (intent != null) {
            startActivity(intent) // Intent가 null이 아닐 때만 Activity 시작
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {
        val route = when (screenType) {
            ScreenType.BOOT -> "bootScreen"
            ScreenType.ENTRY -> "entryScreen"
            else -> return
        }
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}