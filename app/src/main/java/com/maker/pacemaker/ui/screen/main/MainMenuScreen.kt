package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainMenuScreenViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMenuScreenViewModel

@Composable
fun MainMenuScreen(baseViewModel: BaseViewModel, mainViewModel: MainBaseViewModel, viewModel: MainMenuScreenViewModel) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비
    val boxHeight = screenHeight - (40.dp) // 위아래 20.dp씩 빼기
    val boxWidth = ( screenWidth / 3 ) * 2

    ConstraintLayout (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF14299F))
        ) {
        val (leftBar, exitButton, navBox, settingButton) = createRefs()

        Log.d("MainMenuScreen", "this is MainMenuScreen")

        Box(
           modifier = Modifier
               .background(Color.White)
               .width(boxWidth)
               .height(boxHeight)
               .constrainAs(leftBar) {
                   start.linkTo(parent.start, margin = 20.dp)
                   top.linkTo(parent.top)
                   bottom.linkTo(parent.bottom)
               }
        )

        Image(
            painter = painterResource(id = R.drawable.exit),
            contentDescription = "Ranking",
            modifier = Modifier
                .size(30.dp)
                .constrainAs(exitButton) {
                    end.linkTo(parent.end, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                }
                .clickable {
                    mainViewModel.goScreen(ScreenType.MAIN)
                }
        )

        Column(
            modifier = Modifier
                .constrainAs(navBox) {
                    start.linkTo(leftBar.start, margin = 20.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ){
            Text(
                text = "내 정보",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .clickable(onClick = {
                        baseViewModel.goScreen(ScreenType.MYPAGE)
                    })
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "자가 진단",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "알림",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .clickable(onClick = {
                        baseViewModel.goScreen(ScreenType.ALARM)
                    })
            )
        }

        Row(
            modifier = Modifier
                .constrainAs(settingButton) {
                    start.linkTo(leftBar.start, margin = 20.dp)
                    bottom.linkTo(leftBar.bottom, margin = 20.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.setting),
                contentDescription = "Setting",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        Log.d("MainMenuScreen", "Setting Button Clicked")
                    }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "설정",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

    }
}

@Composable
@Preview
fun MainMenuScreenPreview() {

    val baseViewModel = DummyBaseViewModel()
    val mainViewModel = DummyMainBaseViewModel()
    val viewModel = DummyMainMenuScreenViewModel()

    MainMenuScreen(baseViewModel, mainViewModel, viewModel)
}