package com.maker.pacemaker.ui.activity.signup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity

import com.maker.pacemaker.ui.screen.signup.SignUpLoadScreen
import com.maker.pacemaker.ui.screen.signup.SignUpScreen
import com.maker.pacemaker.ui.viewmodel.signup.details.SignUpLoadScreenViewModel
import com.maker.pacemaker.ui.viewmodel.signup.details.SignUpScreenViewModel

class SignUpActivity : BaseActivity() {

    private val signUpScreenViewModel : SignUpScreenViewModel by viewModels()
    private val signUpLoadScreenViewModel : SignUpLoadScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "signUpScreen") {
                composable("signUpScreen") { SignUpScreen(signUpScreenViewModel) }
                composable("signUpLoadScreen") { SignUpLoadScreen(signUpLoadScreenViewModel) }
            }
        }
    }

    override fun navigateToActivity(activityType: ActivityType) {
        val intent = activityType.intentCreator(this)
        if (activityType == ActivityType.FINISH) {
            finish() // 현재 Activity 종료
        } else if (intent != null) {
            finish()
            startActivity(intent) // Intent가 null이 아닐 때만 Activity 시작
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {
        when (screenType) {
            ScreenType.SIGNUP -> "signUpScreen"
            ScreenType.SIGNUPLOAD ->"signUpLoadScreen"
            else -> return
        }
    }

}