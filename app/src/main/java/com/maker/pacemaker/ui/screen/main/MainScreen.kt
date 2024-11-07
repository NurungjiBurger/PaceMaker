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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.ui.screen.Component.BottomNavBar
import com.maker.pacemaker.ui.screen.Component.Loading
import com.maker.pacemaker.ui.screen.Component.Logo
import com.maker.pacemaker.ui.screen.Component.Rating
import com.maker.pacemaker.ui.screen.Component.TopNavBar


@Composable
fun MainScreen(viewModel: MainScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val user by baseViewModel.userInfo.collectAsState()
    val isLoading by baseViewModel.isLoading.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, greeting, rating, contentBox, bottomBar) = createRefs()

        // 상단바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .constrainAs(upBar) {
                    top.linkTo(parent.top)
                }
        ) {
            TopNavBar(baseViewModel)
        }

        // 환영 메시지와 사용자 이름
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .constrainAs(greeting) {
                    top.linkTo(upBar.bottom, margin = 16.dp)
                },
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "안녕하세요 ${user.nickname}님",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "오늘도 재밌는 퀴즈 풀어요~",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        // 랭킹 정보
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .constrainAs(rating) {
                    top.linkTo(greeting.bottom, margin = 16.dp)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Rating", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "상위 1%", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            // 모자로 바꾸기
        }

        // 콘텐츠 Box 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .constrainAs(contentBox) {
                    top.linkTo(rating.bottom, margin = 16.dp)
                    bottom.linkTo(bottomBar.top, margin = 16.dp)
                },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1429A0))
                    .clickable { baseViewModel.goScreen(ScreenType.PROBLEMSOLVE) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    // 왼쪽 이미지
                    Image(
                        painter = painterResource(id = R.drawable.light1),
                        contentDescription = "Icon 1",
                        modifier = Modifier.size(55.dp)
                    )

                    // 텍스트
                    Text(
                        text = "오늘의 학습",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // 오른쪽 이미지
                    Image(
                        painter = painterResource(id = R.drawable.light2),
                        contentDescription = "Icon 2",
                        modifier = Modifier.size(55.dp)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFDFE9FE))
                    .clickable { baseViewModel.goScreen(ScreenType.LAB) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)

                ) {
                    // 왼쪽 이미지
                    Image(
                        painter = painterResource(id = R.drawable.interviewer1),
                        contentDescription = "Icon 1",
                        modifier = Modifier.size(48.dp)
                    )

                    // 텍스트
                    Text(
                        text = "CS 면접",
                        color = Color(0xFF1429A0),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // 오른쪽 이미지
                    Image(
                        painter = painterResource(id = R.drawable.interviewer2),
                        contentDescription = "Icon 2",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF5387F7))
                    .clickable { baseViewModel.goScreen(ScreenType.LAB) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "싸 맨 틀", color = Color(0xFF1429A0), fontSize = 20.sp)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFFFFF))
                    .border(BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(12.dp)) // 테두리 두께와 색상 조정
                    //.shadow(8.dp, shape = RoundedCornerShape(12.dp), clip = false) // 그림자 추가
                    .clickable { baseViewModel.goScreen(ScreenType.LAB) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "문제 추가하기", color = Color.Black, fontSize = 18.sp)
            }

        }

        // 하단바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .constrainAs(bottomBar) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            BottomNavBar(baseViewModel)
        }
    }

    // 로딩 다이얼로그
    isLoading?.let { Loading("로딩 중...", isLoading = it, onDismiss = { /* Dismiss Logic */ }) }
}
