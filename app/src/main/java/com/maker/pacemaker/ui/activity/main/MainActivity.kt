package com.maker.pacemaker.ui.activity.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.ui.activity.BaseActivity
import com.maker.pacemaker.ui.screen.main.MainScreen
import com.maker.pacemaker.ui.screen.main.MainTestScreen
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainTestScreenViewModel

class MainActivity : BaseActivity() {

    private lateinit var mainViewModel: MainBaseViewModel
    private lateinit var mainScreenViewModel: MainScreenViewModel
    private lateinit var mainTestScreenViewModel: MainTestScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainBaseViewModel::class.java)
        mainScreenViewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        mainTestScreenViewModel = ViewModelProvider(this).get(MainTestScreenViewModel::class.java)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("mainScreen") {
                    MainScreen(baseViewModel, mainViewModel, mainScreenViewModel) // BaseViewModel 전달
                }
                composable("testScreen") { MainTestScreen(baseViewModel, mainViewModel, mainTestScreenViewModel) }
            }
        }
    }

    // NavController 초기화 메서드 구현
    override fun initNavController(): NavHostController {
        return navController as NavHostController
    }

    override fun navigateToScreen(screenType: ScreenType) {
        val route = when (screenType) {
            ScreenType.MAIN -> "mainscreen"
            ScreenType.TEST -> "testScreen"
            else -> return
        }
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}