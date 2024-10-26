package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemAddScreenViewModel

@Composable
fun MainProblemAddScreen(viewModel: MainProblemAddScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val problem by viewModel.problem.collectAsState()
    val answer by viewModel.answer.collectAsState()
    val keyWord by viewModel.keyWord.collectAsState()
    val keyWords by viewModel.keyWords.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, contentBox, enrollButton) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        ) {
            UpBar(baseViewModel, "문제 추가", false, ActivityType.FINISH, ScreenType.MAIN)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, 15.dp)
                    bottom.linkTo(enrollButton.top, 15.dp)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "문제 작성",
                    fontSize = 25.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = "${problem.length}",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
                Text(
                    text = "/3000",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = problem,
                onValueChange = { viewModel.onProblemChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 3)
                    .background(Color.White)
                    .border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp)),
                label = { Text("문제를 입력해주세요") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "정답 설정",
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = answer,
                onValueChange = { viewModel.onAnswerChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp)),
                label = { Text("정답을 입력해주세요") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "키워드 설정",
                    fontSize = 25.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "등록",
                    fontSize = 25.sp,
                    color = Color(0xFF1429A0),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.onKeyWordEnroll()
                    }
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = keyWord,
                onValueChange = { viewModel.onKeyWordChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
                    .border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp)),
                label = { Text("키워드를 입력해주세요") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            // 등록된 키워드 표시 부분
            if (keyWords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    keyWords.forEach { keyword ->
                        Text(
                            text = keyword,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(4.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp) // 키워드 내부 여백
                        )
                    }
                }
            }
        }

        // 등록 버튼 부분
        if (problem.isNotEmpty() && answer.isNotEmpty() && keyWords.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
                    .height(70.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF000000), shape = RoundedCornerShape(10.dp))
                    .constrainAs(enrollButton) {
                        top.linkTo(contentBox.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 20.dp)
                        end.linkTo(parent.end, margin = 20.dp)
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                    }
                    .clickable {
                        viewModel.onSubmit()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}