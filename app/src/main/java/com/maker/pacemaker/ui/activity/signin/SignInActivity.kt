package com.maker.pacemaker.ui.activity.signin

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
import com.maker.pacemaker.ui.screen.signin.LogInScreen
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.details.SignInScreenViewModel


class SignUpActivity : BaseActivity() {

    private lateinit var signInViewModel : SignInBaseViewModel

    private val signInScreenViewModel : SignInScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var signinViewModel = ViewModelProvider(this).get(SignInBaseViewModel::class.java)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "signinScreen") {
                composable("signinScreen") { LogInScreen(baseViewModel, signInViewModel, signInScreenViewModel) }

            }
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {
        when (screenType) {
            ScreenType.SIGNIN -> "loadScreen"
            else -> return
        }
    }

    override fun initNavController(): NavController {
        return navController
    }

}