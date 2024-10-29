package com.maker.pacemaker.ui.activity.signin

import android.os.Bundle
import android.util.Log
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

import com.maker.pacemaker.ui.screen.signin.SignInLoadScreen
import com.maker.pacemaker.ui.screen.signin.SignInScreen
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.details.SignInScreenViewModel


class SignInActivity : BaseActivity() {

    private val signInScreenViewModel : SignInScreenViewModel by viewModels()
    private val signInLoadScreenViewModel : SignInScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "signInScreen") {
                composable("signInScreen") { SignInScreen(signInScreenViewModel) }
                composable("signInLoadScreen") { SignInLoadScreen(signInLoadScreenViewModel) }

            }
        }
    }

    override fun navigateToActivity(activityType: ActivityType) {

        Log.d("MainActivity", "navigateToActivity: $activityType")

        if (baseViewModel.previousActivity != ActivityType.SIGNIN) return

        val intent = activityType.intentCreator(this)
        if (activityType == ActivityType.FINISH) {
            finish() // 현재 Activity 종료
        } else if (intent != null) {
            startActivity(intent) // Intent가 null이 아닐 때만 Activity 시작
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {
        when (screenType) {
            ScreenType.SIGNIN -> "signInScreen"
            ScreenType.SIGNINLOAD -> "signInLoadScreen"
            else -> return
        }
    }

}