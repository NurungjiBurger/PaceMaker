package com.maker.pacemaker.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
            onDismissRequest = {
                solvedDialog = false
            },
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
                        painter = painterResource(id = R.drawable.done), // 엄지 척 아이콘 리소스
                        contentDescription = "Thumb Up",
                        tint = Color(0xFFFFCC00),
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
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
                    onClick = {
                        solvedDialog = false
                    },
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
            .background(color = Color(0xFFFAFAFA))
    ) {

        val (upBar, contentBox, myBox) = createRefs()

            // 상단 메달과 텍스트
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .constrainAs(upBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 16.dp)
                    }
            ) {
                Text(
                    text = "순위를\n확인해볼까요?",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.medal),
                    contentDescription = "Medal",
                    modifier = Modifier.size(100.dp)
                )
            }

        // 하단 UI 추가
        Column(
            modifier = Modifier
                .width(330.dp)
                .background(Color(0xFFFAFAFA))
                .constrainAs(myBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 15.dp)
                    bottom.linkTo(contentBox.top)
                }
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "나의 랭킹",
                fontSize = 25.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "${myUserInfo?.index?.plus(1)}", // 예시 랭킹 숫자
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "${myUserInfo?.nickname}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "${myUserInfo?.try_cnt}회", // 예시 도전 횟수
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }


        // LazyColumn을 ConstraintLayout 내에서 직접 사용
        LazyColumn(
            modifier = Modifier
                .width(330.dp)
                .height(screenHeight / 2)
                .padding(16.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFDFE9FE))
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(myBox.bottom, margin = 15.dp)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "이름",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                    Text(
                        text = "시도 횟수",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            items(userRank.size) { index ->
                val rank = userRank[index]

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .height(40.dp)
                        .background(Color.White)
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .width(30.dp)
                            .padding(start = 8.dp)
                    )

                    Text(
                        text = rank.nickname,
                        fontSize = 18.sp,
                        maxLines = 1,
                        softWrap = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )

                    Text(
                        text = "${rank.try_cnt}회",
                        fontSize = 18.sp,
                        modifier = Modifier.width(50.dp)
                    )
                }
            }
        }



    }
}

