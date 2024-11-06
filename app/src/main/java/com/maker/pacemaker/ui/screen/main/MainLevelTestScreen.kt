package com.maker.pacemaker.ui.screen.main

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.viewmodel.main.details.MainLevelTestScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLevelTestScreen(viewModel: MainLevelTestScreenViewModel) {

    // ViewModel에서 가져오는 데이터들
    val timeLimit = viewModel.timeLimit.collectAsState()
    val level = viewModel.level.collectAsState()
    val testProblems = viewModel.testProblems.collectAsState()
    val nowProblemIndex = viewModel.nowProblemIndex.collectAsState()
    val wrongCnt = viewModel.wrongCnt.collectAsState()
    val circleStates = viewModel.circleStates.collectAsState()

    val clear by viewModel.clear.collectAsState()
    val ment by viewModel.ment.collectAsState()
    val answer by viewModel.answer.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 화면 너비

    val keyboardController = LocalSoftwareKeyboardController.current


    // 화면 전환을 위한 LaunchedEffect
    LaunchedEffect(clear) {
        if (clear) {
            viewModel.changeScreen()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, divider, timer, step, contentBox, submitButton) = createRefs()

        // 상단 바
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            Text(
                text = "자가 진단",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        // 구분선
        Box(
            modifier = Modifier
                .width(screenWidth - 60.dp)
                .height(1.dp)
                .background(Color.Gray)
                .padding(start = 40.dp, end = 40.dp)
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 5.dp)
                }
        )

        if (clear) {

            // 레벨 테스트 완료 시
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2)
                    .padding(30.dp)
                    .constrainAs(contentBox) {
                        top.linkTo(divider.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ment,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(R.drawable.mascot),
                    contentDescription = "check",
                    modifier = Modifier
                        .size(200.dp)
                )
            }

        } else {

            // 제한시간 표시
            Text(
                text = "남은 시간: ${timeLimit.value}초",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(30.dp)
                    .constrainAs(timer) {
                        top.linkTo(upBar.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            // 문제 진행 상태 (동그라미)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .constrainAs(step) {
                        top.linkTo(timer.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // 문제 수만큼 동그라미 표시
                testProblems.value.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .background(
                                if (circleStates.value[index] == 0) Color.White
                                else if (circleStates.value[index] == 1) Color.Green
                                else Color.Red,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .border(
                                1.dp,
                                if (circleStates.value[index] == 0) Color.Black
                                else if (circleStates.value[index] == 1) Color.Green
                                else Color.Red,
                                shape = RoundedCornerShape(50.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }

            // 문제와 입력란
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .constrainAs(contentBox) {
                        top.linkTo(step.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 현재 문제 표시
                val currentProblem = testProblems.value.getOrNull(nowProblemIndex.value)
                Text(
                    text = "문제: ${currentProblem?.description}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 답안 입력란
                TextField(
                    value = answer,
                    onValueChange = { viewModel.onAnswerChanged(it) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    label = {
                        Text(text = "정답", color = Color.Black)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            keyboardController?.hide() // 키패드 숨기기
                            viewModel.onSubmit()
                        }
                    )
                )
            }

            // 제출 버튼
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                    .constrainAs(submitButton) {
                        top.linkTo(contentBox.bottom, margin = 10.dp)
                        end.linkTo(parent.end, margin = 30.dp)
                    }
                    .clickable {
                        viewModel.onSubmit() // 제출 버튼 클릭 시
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "제출",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
