package com.maker.pacemaker.ui.activity.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.main.MainAlarmScreen
import com.maker.pacemaker.ui.screen.main.MainCSMantleRankingScreen
import com.maker.pacemaker.ui.screen.main.MainCSMantleScreen
import com.maker.pacemaker.ui.screen.main.MainDoneScreen
import com.maker.pacemaker.ui.screen.main.MainLabScreen
import com.maker.pacemaker.ui.screen.main.MainLevelTestScreen
import com.maker.pacemaker.ui.screen.main.MainMenuScreen
import com.maker.pacemaker.ui.screen.main.MainProblemAddScreen
import com.maker.pacemaker.ui.screen.main.MainProblemSearchScreen
import com.maker.pacemaker.ui.screen.main.MainProblemSolveScreen
import com.maker.pacemaker.ui.screen.main.MainRankingScreen
import com.maker.pacemaker.ui.screen.main.MainScreen
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainCSMantleRankingScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainCSMantleScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainDoneScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainLabScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainLevelTestScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMenuScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemAddScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSearchScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSolveScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainRankingScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val mainAlarmScreenViewModel: MainAlarmScreenViewModel by viewModels()
    private val mainMenuScreenViewModel: MainMenuScreenViewModel by viewModels()
    private val mainLevelTestScreenViewModel: MainLevelTestScreenViewModel by viewModels()
    private val mainProblemAddScreenViewModel: MainProblemAddScreenViewModel by viewModels()
    private val mainProblemSearchScreenViewModel: MainProblemSearchScreenViewModel by viewModels()
    private val mainProblemSolveScreenViewModel: MainProblemSolveScreenViewModel by viewModels()
    private val mainRankingScreenViewModel: MainRankingScreenViewModel by viewModels()
    private val mainLabScreenViewModel: MainLabScreenViewModel by viewModels()
    private val mainCSMantleScreenViewModel: MainCSMantleScreenViewModel by viewModels()
    private val mainDoneScreenViewModel: MainDoneScreenViewModel by viewModels()
    private val mainCSMantleRankingScreenViewModel: MainCSMantleRankingScreenViewModel by viewModels()

    private val alarmUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            mainAlarmScreenViewModel.reloadAlarms() // 알람 갱신
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(alarmUpdateReceiver, IntentFilter("com.maker.pacemaker.ALARM_UPDATED"))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(alarmUpdateReceiver)
    }

    private val mainViewModel: MainBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("mainScreen") { MainScreen(mainScreenViewModel) }
                composable("alarmScreen") { MainAlarmScreen(mainAlarmScreenViewModel) }
                composable("menuScreen") { MainMenuScreen(mainMenuScreenViewModel) }
                composable("levelTestScreen") {
                    mainLevelTestScreenViewModel.restate()
                    mainLevelTestScreenViewModel.resumeTimer()
                    MainLevelTestScreen(mainLevelTestScreenViewModel)
                }
                composable("problemAddScreen") { MainProblemAddScreen(mainProblemAddScreenViewModel) }
                composable("problemSearchScreen") {
                    mainProblemSearchScreenViewModel.restate()
                    MainProblemSearchScreen(mainProblemSearchScreenViewModel)
                }
                composable("problemSolveScreen") { MainProblemSolveScreen(mainProblemSolveScreenViewModel) }
                composable("rankingScreen") {
                    mainRankingScreenViewModel.restate()
                    MainRankingScreen(mainRankingScreenViewModel)
                }
                composable("labScreen") { MainLabScreen(mainLabScreenViewModel) }
                composable("csMantleScreen") { MainCSMantleScreen(mainCSMantleScreenViewModel) }
                composable("doneScreen") { MainDoneScreen(mainDoneScreenViewModel)}
                composable("csRankingScreen") {MainCSMantleRankingScreen(mainCSMantleRankingScreenViewModel)}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        baseViewModel.restate()
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
            ScreenType.MAIN -> "mainScreen"
            ScreenType.ALARM -> "alarmScreen"
            ScreenType.MENU -> "menuScreen"
            ScreenType.LEVELTEST -> "levelTestScreen"
            ScreenType.PROBLEMADD -> "problemAddScreen"
            ScreenType.PROBLEMSEARCH -> "problemSearchScreen"
            ScreenType.PROBLEMSOLVE -> "problemSolveScreen"
            ScreenType.RANKING -> "rankingScreen"
            ScreenType.LAB -> "labScreen"
            ScreenType.CSMANTLE -> "csMantleScreen"
            ScreenType.DONE -> "doneScreen"
            ScreenType.CSRANKING -> "csRankingScreen"
            else -> return
        }

        navController.navigate(route) {
            // 기존 페이지는 스택에서 제외하고 바로 이동
            if (screenType == ScreenType.CSRANKING) {
                popUpTo("csMantleScreen") { inclusive = true } // CSMantleScreen을 포함하여 제거
            }
            else {
                popUpTo(route) {
                    inclusive = true // 포함하여 제거
                }
            }
            launchSingleTop = true
        }
    }
}