package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.background
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
        viewModel.showRanking() // 화면 표시 시 실행하고자 하는 함수 호출
    }

    ConstraintLayout(

        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {

        val (contentBoxBorder, contentBox) = createRefs()
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
                .fillMaxWidth()
                .height(screenHeight / 2)
                .padding(start = 15.dp)
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 100.dp) // Column과 LazyColumn 사이의 여백을 지정
                    bottom.linkTo(parent.bottom)
                }
        ) {
            // 유사도 순으로 내림차순 정렬
            val sortedRanks = userRank.sortedBy { it.try_cnt } ?: emptyList()

            items(sortedRanks.size) { index ->
                val rank = sortedRanks[index]

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(30.dp)
                    )

                    Text(
                        text = rank.nickname,
                        fontSize = 18.sp,
                        maxLines = 2,
                        softWrap = true,
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .weight(1f)
                            .padding(start = 8.dp)
                    )

                    Text(
                        text = "${rank.try_cnt}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(end = 52.dp)
                            .alignBy { it.measuredHeight / 2 }
                    )
                }
            }
        }
    }
}
