package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.main.details.MainLabScreenViewModel

@Composable
fun MainLabScreen(viewModel: MainLabScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val boxHeight = screenHeight / 10
    val boxWidth = screenWidth  - 80.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, listBox) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            if (baseViewModel.previousActivity != ActivityType.FINISH) baseViewModel.previousActivity?.let {
                Log.d("MainAlarmScreen", "previousActivity: $it")
                UpBar(
                    baseViewModel, "실험실", true,
                    it, ScreenType.FINISH
                )
            }
            else if (baseViewModel.previousScreen != ScreenType.FINISH) baseViewModel.previousScreen?.let {
                Log.d("MainAlarmScreen", "previousScreen: $it")
                UpBar(
                    baseViewModel, "실험실", false, ActivityType.FINISH,
                    it
                )
            }
        }

        Column(
           verticalArrangement = Arrangement.spacedBy(30.dp),
           horizontalAlignment = Alignment.CenterHorizontally,
           modifier = Modifier
               .fillMaxWidth()
                .constrainAs(listBox) {
                     start.linkTo(parent.start)
                     end.linkTo(parent.end)
                     top.linkTo(upBar.bottom, margin = 30.dp)
                     bottom.linkTo(parent.bottom)
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(boxWidth)
                    .height(boxHeight)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "싸맨틀",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(boxWidth)
                    .height(boxHeight)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "문제 추가하기",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(boxWidth)
                    .height(boxHeight)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = "CS면접",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Image(
                painter = painterResource(id = R.drawable.mascot),
                contentDescription = "mascot",
                modifier = Modifier
                    .width(screenWidth / 2)
                    .height(screenWidth / 2)
            )

            Text(
                text = "아직 준비중인\n서비스들이에요.",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}