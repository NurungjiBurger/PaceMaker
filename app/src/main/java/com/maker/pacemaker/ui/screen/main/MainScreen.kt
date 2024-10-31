package com.maker.pacemaker.ui.screen.main

import android.app.Application
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.screen.Component.BottomNavBar
import com.maker.pacemaker.ui.screen.Component.Logo
import com.maker.pacemaker.ui.screen.Component.Rating
import com.maker.pacemaker.ui.screen.Component.TopNavBar


@Composable
fun MainScreen(viewModel: MainScreenViewModel) {

    LaunchedEffect(Unit) {
        Log.e("identifyUserByToken", "들어왔어요.")
        viewModel.identifyUserByToken()
    }

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val userName by baseViewModel.userName.collectAsState()


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        // Constraints 정의
        val (upBar, logo, script, ranking, contentBox, bottomBar) = createRefs()

        // 상단바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        ) {
            TopNavBar(baseViewModel)
        }

        // 로고
        Box(
            modifier = Modifier
                .size(60.dp)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 30.dp)
                }
        ) {
            Logo(baseViewModel)
        }

        // 스크립트
        Box(
            modifier = Modifier
                .constrainAs(script) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logo.bottom, margin = 20.dp)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "안녕하세요",
                    fontSize = 25.sp,
                )

                Text(
                    text = "${userName}님",
                    fontSize = 40.sp,

                )
            }
        }

        // 랭킹
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .constrainAs(ranking) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(script.bottom, margin = 20.dp)
                }
        )
        {
            Rating(baseViewModel)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(ranking.bottom)
                    bottom.linkTo(bottomBar.top)
                },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFF1429A0), RoundedCornerShape(10.dp))
                        .border(BorderStroke(2.dp, Color.Blue), shape = RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { baseViewModel.goScreen(ScreenType.PROBLEMSOLVE) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "오늘의 학습",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFF1429A0), RoundedCornerShape(10.dp))
                        .border(BorderStroke(2.dp, Color.Blue), shape = RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { baseViewModel.goScreen(ScreenType.LAB) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CS 면접",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFFEFEFF).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.LAB) }
                ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "문제 추가하기",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFFEFEFF).copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { baseViewModel.goScreen(ScreenType.LAB) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "싸맨틀",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }


        // 하단바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .constrainAs(bottomBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            BottomNavBar(baseViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    // MockApplication 대신 Application 컨텍스트를 직접 전달합니다.


    // 모든 더미 ViewModel을 전달하여 미리보기 실행
   // MainScreen(dummyBaseViewModel, dummyMainBaseViewModel, dummyMainScreenViewModel)
}

