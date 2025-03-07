package com.maker.pacemaker.ui.activity.interview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.maker.pacemaker.ui.activity.main.MainActivity
import com.maker.pacemaker.ui.screen.interview.InterviewResultScreen
import com.maker.pacemaker.ui.screen.interview.InterviewStartScreen
import com.maker.pacemaker.ui.screen.interview.InterviewingScreen
import com.maker.pacemaker.ui.screen.main.MainAlarmScreen
import com.maker.pacemaker.ui.screen.main.MainCSMantleScreen
import com.maker.pacemaker.ui.screen.main.MainLabScreen
import com.maker.pacemaker.ui.screen.main.MainLevelTestScreen
import com.maker.pacemaker.ui.screen.main.MainMenuScreen
import com.maker.pacemaker.ui.screen.main.MainProblemAddScreen
import com.maker.pacemaker.ui.screen.main.MainProblemSearchScreen
import com.maker.pacemaker.ui.screen.main.MainProblemSolveScreen
import com.maker.pacemaker.ui.screen.main.MainRankingScreen
import com.maker.pacemaker.ui.screen.main.MainScreen
import com.maker.pacemaker.ui.viewmodel.interview.details.InterviewResultScreenViewModel
import com.maker.pacemaker.ui.viewmodel.interview.details.InterviewStartScreenViewModel
import com.maker.pacemaker.ui.viewmodel.interview.details.InterviewingScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainCSMantleScreenViewModel
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
class InterviewActivity : BaseActivity() {

    private val interviewStartScreenViewModel: InterviewStartScreenViewModel by viewModels()
    private val interviewingScreenViewModel: InterviewingScreenViewModel by viewModels()
    private val interviewResultScreenViewModel: InterviewResultScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "interviewStartScreen") {
                composable("interviewStartScreen") { InterviewStartScreen(interviewStartScreenViewModel) }
                composable("interviewingScreen") {
                    interviewingScreenViewModel.restate()
                    InterviewingScreen(interviewingScreenViewModel)
                }
                composable("interviewResultScreen") {
                    interviewResultScreenViewModel.onRefresh()
                    InterviewResultScreen(interviewResultScreenViewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // TTS와 STT 관련 작업 중단
        interviewingScreenViewModel.pauseOperations()
    }

    override fun onStop() {
        super.onStop()
        // TTS와 STT 관련 작업 중단
        interviewingScreenViewModel.stopOperations()
    }

    override fun onResume() {
        super.onResume()
        baseViewModel.restate()

        Log.d("InterviewActivity", "onResume: ${baseViewModel.screenNavigationTo.value?.screenType}")

        //if (baseViewModel.screenNavigationTo.value?.screenType == ScreenType.INTERVIEWING) {
            interviewingScreenViewModel.resumeOperations()
            Log.d("InterviewActivity", "onResume: resumeOperations")
        //}
    }

    override fun onBackPressed() {
        // InterviewingScreen에서 뒤로가기 작동 X
        if (navController.currentDestination?.route == "interviewingScreen") {
            Toast.makeText(this, "인터뷰 중에는 뒤로가기를 할 수 없습니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 다른 화면에서 뒤로가기 시 MainActivity로 이동
            super.onBackPressed()
        }
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
            ScreenType.INTERVIEWSTART -> "interviewStartScreen"
            ScreenType.INTERVIEWING -> "interviewingScreen"
            ScreenType.INTERVIEWRESULT -> "interviewResultScreen"
            else -> return
        }

        navController.navigate(route) {
            when (screenType) {
                ScreenType.INTERVIEWING -> {
                    // InterviewingScreen을 스택에서 제거
                    popUpTo("interviewStartScreen") {
                        inclusive = false // Start는 유지
                    }
                }
                ScreenType.INTERVIEWRESULT -> {
                    // InterviewResult는 Start로 돌아갈 수 있도록 설정
                    popUpTo("interviewStartScreen") {
                        inclusive = false // Start는 유지
                    }
                }
                else -> {
                    // 기본 popUpTo 동작 없음
                }
            }
            launchSingleTop = true // 중복된 화면 생성 방지
        }
    }
}