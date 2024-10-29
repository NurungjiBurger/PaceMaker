package com.maker.pacemaker.ui.activity.boot

import android.os.Bundle
import androidx.activity.compose.setContent
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
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.boot.BootScreen
import com.maker.pacemaker.ui.screen.boot.EntryScreen

@AndroidEntryPoint
class BootActivity : BaseActivity() {


    private lateinit var bootViewModel: BootBaseViewModel
    private lateinit var bootScreenViewModel: BootScreenViewModel
    private lateinit var entryScreenViewModel: EntryScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bootViewModel = ViewModelProvider(this).get(BootBaseViewModel::class.java)
        bootScreenViewModel = ViewModelProvider(this).get(BootScreenViewModel::class.java)
        entryScreenViewModel = ViewModelProvider(this).get(EntryScreenViewModel::class.java)

        setContent {
            // rememberNavController는 @Composable 함수이므로 여기서 호출해야 합니다.
            navController = rememberNavController()

            NavHost(navController as NavHostController, startDestination = "mainScreen") {
                composable("mainScreen") {
                    BootScreen(
                        baseViewModel,
                        bootViewModel,
                        bootScreenViewModel
                    ) // BaseViewModel 전달
                }
                composable("entryScreen") {
                    EntryScreen(
                        baseViewModel,
                        bootViewModel,
                        entryScreenViewModel
                    ) // BaseViewModel 전달
                }
            }
        }
    }

    override fun navigateToScreen(screenType: ScreenType) {
        val route = when (screenType) {
            ScreenType.MAIN -> "mainscreen"
            ScreenType.ALARM -> "alarmscreen"
            else -> return
        }
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}