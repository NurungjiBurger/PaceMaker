package com.maker.pacemaker.ui.activity.signup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity

import com.maker.pacemaker.ui.screen.sign.SignUpLoadScreen
import com.maker.pacemaker.ui.screen.sign.SignUpScreen
import com.maker.pacemaker.ui.viewmodel.sign.SignBaseViewModel
import com.maker.pacemaker.ui.viewmodel.sign.details.SignUpLoadScreenViewModel
import com.maker.pacemaker.ui.viewmodel.sign.details.SignUpScreenViewModel

class SignUpActivity : BaseActivity() {

    private lateinit var signViewModel : SignBaseViewModel

    private val signUpScreenViewModel : SignUpScreenViewModel by viewModels()
    private val signUpLoadScreenViewModel : SignUpLoadScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "signUpScreen") {
                composable("signUpScreen") {
                    SignUpScreen(
                        baseViewModel,
                        signViewModel,
                        signUpScreenViewModel
                    )
                }
                composable("signUpLoadScreen") {
                    SignUpLoadScreen(
                        baseViewModel,
                        signUpScreenViewModel,
                        signUpLoadScreenViewModel
                    )
                }
            }
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