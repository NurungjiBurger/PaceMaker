package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.viewmodel.main.details.MainDoneScreenViewModel

@Composable
fun MainDoneScreen(viewModel: MainDoneScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000)), // 반투명 배경
        contentAlignment = Alignment.Center
    ) {
        // 메시지 박스
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            // 닫기 버튼
            IconButton(
                onClick = { /* Dismiss Logic */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.xbutton), // 닫기 버튼 아이콘 리소스
                    contentDescription = "Close",
                    tint = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
            ) {
                // 아이콘 (엄지 척)
                Icon(
                    painter = painterResource(id = R.drawable.done), // 엄지 척 아이콘 리소스
                    contentDescription = "Thumb Up",
                    tint = Color(0xFFFFCC00),
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 메시지 텍스트
                Text(
                    text = "오늘의 문제는 모두 풀었어요.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 추가 설명 텍스트
                Text(
                    text = "문제는 한국 표준(KST) 기준 자정에 바뀝니다.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
