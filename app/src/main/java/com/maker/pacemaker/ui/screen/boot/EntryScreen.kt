package com.maker.pacemaker.ui.screen.boot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.boot.BootBaseViewModel
import com.maker.pacemaker.ui.viewmodel.boot.details.EntryScreenViewModel

@Composable
fun EntryScreen(viewModel: EntryScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val bootViewModel = viewModel.bootViewModel

    Box(modifier = Modifier.fillMaxSize()) {
        // 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.study), // 배경 이미지 리소스 ID
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.5f), // 50% 투명도 설정
            contentScale = ContentScale.Crop // 이미지가 부모의 크기에 맞게 잘림
        )

        // 텍스트 및 버튼 배치
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (titleText, subtitleText, signUpButton, loginButton) = createRefs()

            // 제목 텍스트
            Text(
                text = "일상에서 배우는\n나만의 즐거움",
                fontSize = 43.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A4A4A),
                modifier = Modifier
                    .constrainAs(titleText) {
                        top.linkTo(parent.top, margin = 100.dp)
                        start.linkTo(parent.start) // 왼쪽 마진 제거
                        end.linkTo(parent.end) // 오른쪽 마진을 0으로 설정
                    }
            )



            Button(
                onClick = {
                    baseViewModel.goActivity(ActivityType.SIGNUP)
                    },
                modifier = Modifier
                    .constrainAs(signUpButton) {
                        bottom.linkTo(loginButton.top, margin = 16.dp) // 기존 유저 로그인 버튼 위에 위치
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // 좌우에 16dp 패딩 추가
                    .height(50.dp), // 버튼 높이 설정
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1429A0) // 파란색 배경
                ),
                shape = RoundedCornerShape(12.dp) // 버튼 모서리 둥글게
            ) {
                Text(
                    text = "신규 회원가입",
                    color = Color.White,
                    fontWeight = FontWeight.Bold, // 텍스트를 볼드체로 설정
                    fontSize = 20.sp // 폰트 사이즈 설정
                )
            }

// 기존 유저 로그인 버튼
            Button(
                onClick = {
                    baseViewModel.goActivity(ActivityType.SIGNIN)
                },
                modifier = Modifier
                    .constrainAs(loginButton) {
                        bottom.linkTo(parent.bottom, margin = 32.dp) // 화면 하단에서 32dp 위에 위치
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // 좌우에 16dp 패딩 추가
                    .height(50.dp), // 버튼 높이 설정
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp) // 버튼 모서리 둥글게
            ) {
                Text(
                    text = "기존 유저 로그인",
                    color = Color(0xFF1429A0),
                    fontWeight = FontWeight.Bold, // 텍스트를 볼드체로 설정
                    fontSize = 20.sp // 폰트 사이즈 설정
                )
            }


        }
    }
}