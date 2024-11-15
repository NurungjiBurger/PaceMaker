package com.maker.pacemaker.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.viewmodel.main.details.MainCSMantleRankingScreenViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainCSMantleRankingScreen(viewModel: MainCSMantleRankingScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val mainViewModel = viewModel.mainViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val userRank by viewModel.userRanks.collectAsState()
    val myUserInfo by viewModel.myUserInfo.collectAsState()

    val solved = baseViewModel.CSMantleSolved.value
    var solvedDialog by remember { mutableStateOf(solved) }

    if (solvedDialog) {
        AlertDialog(
            onDismissRequest = { solvedDialog = false },
            title = {
                Text(
                    text = "오늘의 문제는 모두 풀었어요.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.done),
                        contentDescription = "Thumb Up",
                        tint = Color(0xFFFFCC00),
                        modifier = Modifier.size(64.dp).clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "문제는 한국 표준(KST) 기준 자정에 바뀝니다.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { solvedDialog = false },
                ) {
                    Text("닫기", color = Color.Black)
                }
            },
            containerColor = Color.White
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (happyImage, profileBox, rankingHeader, rankingListBox) = createRefs()

        // Happy 이미지 상단 중앙에 배치
        Image(
            painter = painterResource(id = R.drawable.happy),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .constrainAs(happyImage) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .zIndex(1f) // 이미지를 앞으로 오게 설정
        )

        // Profile, Ranking, and Attempts Section
        Column(
            modifier = Modifier
                .width(350.dp)
                .padding(20.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFF768AFF))
                .constrainAs(profileBox) {
                    top.linkTo(parent.top, margin = 105.dp)
                    //top.linkTo(happyImage.bottom, margin = (-40).dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.White)) {
                            append("${myUserInfo?.index?.plus(1)}") // 등수 부분
                        }
                        withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal, color = Color.White)) {
                            append("등") // "등" 부분
                        }
                    },
                    modifier = Modifier.padding(end = 40.dp) // 텍스트 주위에 패딩 추가
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)) {
                            append("${myUserInfo?.try_cnt}") // 시도 횟수 부분
                        }
                        withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal, color = Color.White)) {
                            append("회") // "회" 부분
                        }
                    },
                    modifier = Modifier.padding(start = 40.dp) // 텍스트 주위에 패딩 추가
                )
            }
            Text(
                text = "00:00:00",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Ranking Header Section
        Row(
            modifier = Modifier
                .width(350.dp)
                .padding(horizontal = 16.dp, vertical = 2.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0x66FFFFFF)) // 40% 투명한 흰색 배경
                .padding(8.dp)
                .constrainAs(rankingHeader) {
                    top.linkTo(profileBox.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "등수",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(start = 10.dp) // "등수" 텍스트에 오른쪽 패딩 추가
            )
            Text(
                text = "닉네임",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp) // "닉네임" 텍스트에 좌우 패딩 추가
            )
            Text(
                text = "시도횟수",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(end = 10.dp) // "시도횟수" 텍스트에 왼쪽 패딩 추가
            )
        }


        // Rankings List Section
        LazyColumn(
            modifier = Modifier
                .width(350.dp)
                .height(450.dp)
                //.height(screenHeight / 2)
                .padding(16.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .constrainAs(rankingListBox) {
                    top.linkTo(rankingHeader.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            items(userRank.size) { index ->
                val rank = userRank[index]

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 15.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold, // 최대한 굵게 설정
                        fontStyle = FontStyle.Italic, // 기울임 효과 적용
                        color = Color.Black,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(60.dp)) // 인덱스와 닉네임 사이 간격 추가

                    Text(
                        text = rank.nickname,
                        fontSize = 16.sp,
                        maxLines = 1,
                        softWrap = true,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )

                    Text(
                        text = "${rank.try_cnt}회",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(end = 10.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }
    }
}
