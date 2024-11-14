package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


@Composable
fun MainCSMantleRankingScreen(viewModel: MainCSMantleRankingScreenViewModel) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val userRank by viewModel.userRanks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.showRanking()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {

        val (contentBox, bottomBox) = createRefs()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 상단 메달과 텍스트
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "순위를 확인해볼까요?",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.medal),
                    contentDescription = "Medal",
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        // LazyColumn을 ConstraintLayout 내에서 직접 사용
        LazyColumn(
            modifier = Modifier
                .width(330.dp)
                .height(screenHeight / 2)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFDFE9FE))
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 50.dp)
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

            // 유사도 순으로 내림차순 정렬
            val sortedRanks = userRank.sortedBy { it.try_cnt } ?: emptyList()

            items(sortedRanks.size) { index ->
                val rank = sortedRanks[index]

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


        // 하단 UI 추가
        Row(
            modifier = Modifier
                .width(330.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFAFAFA))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .constrainAs(bottomBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "나의 랭킹",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "327", // 예시 랭킹 숫자
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "도전 횟수",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "57회", // 예시 도전 횟수
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

