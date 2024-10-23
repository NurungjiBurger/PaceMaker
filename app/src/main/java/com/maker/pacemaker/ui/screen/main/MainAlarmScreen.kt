package com.maker.pacemaker.ui.screen.main

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainAlarmScreenViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel

@Composable
fun MainAlarmScreen(baseViewModel: BaseViewModel, mainViewModel: MainBaseViewModel, viewModel: MainAlarmScreenViewModel) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, contentBox) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom)
                }
        )
        {
            UpBar(baseViewModel, "새 소식", false, ActivityType.FINISH, ScreenType.MAIN)
        }
    }

}

@Composable
@Preview
fun PreviewMainAlarmScreen() {

    val baseViewModel = DummyBaseViewModel()
    val mainViewModel = DummyMainBaseViewModel()
    val mainAlarmScreenViewModel = DummyMainAlarmScreenViewModel()

    MainAlarmScreen(baseViewModel, mainViewModel, mainAlarmScreenViewModel)
}