package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    val baseViewModel = viewModel.baseViewModel
    val mainViewModel = viewModel.mainViewModel

    val allCSMantleSolved by baseViewModel.CSMantleSolved.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val boxHeight = screenHeight / 10
    val boxWidth = screenWidth  - 80.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (upBar, divider, listBox) = createRefs()

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            Text(
                text = "실험실",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(screenWidth - 60.dp)
                .height(1.dp)
                .background(Color.Gray)
                .padding(start = 40.dp, end = 40.dp)
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 5.dp)
                }
        )
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
                    .clickable {if (allCSMantleSolved) baseViewModel.goScreen(ScreenType.CSRANKING) else baseViewModel.goScreen(ScreenType.CSMANTLE)},
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
                    .clickable{baseViewModel.triggerToast("준비중인 서비스입니다.")},
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
                    .clickable(onClick = {
                        baseViewModel.goActivity(ActivityType.INTERVIEW)
                    })
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
                text = "테스트 중인 서비스들입니다.",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}