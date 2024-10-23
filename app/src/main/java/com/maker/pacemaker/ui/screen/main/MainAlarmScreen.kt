package com.maker.pacemaker.ui.screen.main

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainAlarmScreenViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.ui.screen.Component.AlarmBox
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainAlarmScreen(baseViewModel: BaseViewModel, mainViewModel: MainBaseViewModel, viewModel: MainAlarmScreenViewModel) {

    val alarms by viewModel.alarms.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, contentBox) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            UpBar(baseViewModel, "새 소식", false, ActivityType.FINISH, ScreenType.MAIN)
        }

        LazyColumn(
            modifier = Modifier.constrainAs(contentBox) {
                top.linkTo(upBar.bottom)
                bottom.linkTo(parent.bottom)
            }
        ) {
            items(alarms) { alarm ->
                AlarmBox(
                    baseViewModel = baseViewModel,
                    alarmType = alarm.alarmType,
                    content = alarm.content,
                    dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(alarm.dateTime)
                    ),
                    onDismiss = { viewModel.deleteAlarm(alarm) } // 삭제 콜백
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewMainAlarmScreen() {

    val baseViewModel = DummyBaseViewModel()
    val mainViewModel = DummyMainBaseViewModel()
    //val mainAlarmScreenViewModel = DummyMainAlarmScreenViewModel()

    //MainAlarmScreen(baseViewModel, mainViewModel, mainAlarmScreenViewModel)
}