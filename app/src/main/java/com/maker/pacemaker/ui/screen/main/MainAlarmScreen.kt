package com.maker.pacemaker.ui.screen.main

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.AlarmBox
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainAlarmScreen(viewModel: MainAlarmScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val alarms by viewModel.alarms.collectAsState(initial = emptyList())

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, addButton, contentBoxBorder, contentBox) = createRefs()

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
            if (baseViewModel.previousActivity != ActivityType.FINISH) baseViewModel.previousActivity?.let {
                Log.d("MainAlarmScreen", "previousActivity: $it")
                UpBar(baseViewModel, "새 소식", true,
                    it, ScreenType.FINISH)
            }
            else if (baseViewModel.previousScreen != ScreenType.FINISH) baseViewModel.previousScreen?.let {
                Log.d("MainAlarmScreen", "previousScreen: $it")
                UpBar(baseViewModel, "새 소식", false, ActivityType.FINISH,
                    it
                )
            }
        }

        println("now : ${System.currentTimeMillis()}")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .constrainAs(contentBox) {
                top.linkTo(upBar.bottom, margin = 50.dp)
                bottom.linkTo(parent.bottom, margin = 30.dp)
            }
        ) {
            println("Alarms: ${alarms.size}")

            // itemsIndexed를 사용하여 각 아이템의 인덱스 접근
            itemsIndexed(alarms) { index, alarm ->
                AlarmBox(
                    baseViewModel = baseViewModel,
                    alarmId = alarm.id,
                    alarmType = alarm.alarmType,
                    content = alarm.content,
                    dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(alarm.dateTime)),
                    type = index == alarms.size - 1, // 마지막 아이템 여부
                    onDismiss = { viewModel.deleteAlarm(alarm.id) } // 삭제 콜백
                )
            }
        }

        Button(
            onClick = {
                viewModel.addAlarm("학습해~", "공부 안한지 너무 오래되지 않았니?", System.currentTimeMillis() - 86400000) // 알람 추가
            },
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(addButton) {
                    top.linkTo(contentBox.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text(text = "Add Alarm")
        }
    }
}

@Composable
@Preview
fun PreviewMainAlarmScreen() {

    //val mainAlarmScreenViewModel = DummyMainAlarmScreenViewModel()

    //MainAlarmScreen(baseViewModel, mainViewModel, mainAlarmScreenViewModel)
}