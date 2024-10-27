package com.maker.pacemaker.ui.activity.sign

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.sign.LoadScreen
import com.maker.pacemaker.ui.screen.sign.SignAuthScreen
import com.maker.pacemaker.ui.viewmodel.sign.SignBaseViewModel
import com.maker.pacemaker.ui.viewmodel.sign.details.SignUpScreenViewModel

class SignUpActivity : BaseActivity() {

    private lateinit var signViewModel : SignBaseViewModel

    private val signUpScreenViewModel : SignUpScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signViewModel = ViewModelProvider(this).get(SignBaseViewModel::class.java)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "signupScreen") {
                composable("signupScreen") {
                    SignAuthScreen(
                        baseViewModel,
                        signViewModel,
                        signUpScreenViewModel
                    )
                }
                composable("loadScreen") {
                    LoadScreen(
                        baseViewModel,
                        signViewModel,
                        signUpScreenViewModel
                    )
                }
            }
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {
        when (screenType) {
            ScreenType.SIGNUP -> "signupScreen"
            else -> return
        }
    }

    override fun initNavController(): NavController {
        return navController
    }

}