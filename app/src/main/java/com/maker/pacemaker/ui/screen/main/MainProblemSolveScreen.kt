package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.maker.pacemaker.ui.screen.Component.HintCard
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSearchScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSolveScreenViewModel

@Composable
fun MainProblemSolveScreen(viewModel: MainProblemSolveScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val todaySolvedCount by viewModel.todaySolvedCount.collectAsState()
    val todayProblems by viewModel.todayProblems.collectAsState()
    val problemHints by viewModel.problemHints.collectAsState()
    val answer by viewModel.answer.collectAsState()
    val wrongCnt by viewModel.wrongCnt.collectAsState()
    val report by viewModel.report.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val hintWidth = screenWidth - 20.dp
    val hintHeight = screenHeight / 8

    // 다이얼로그 상태 관리
    var selectedProblem by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // 다이얼로그 열기
    if (showDialog && selectedProblem != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false }, // 다이얼로그 외부 클릭 시 닫기
            title = { Text("어떤 문제가 있나요?") },
            text = {
                Column (
                    modifier = Modifier
                        .height(screenHeight / 2)
                ) {
                    TextField(
                        value = report,
                        onValueChange = { viewModel.onReportChanged(it) },
                        label = { Text("내용을 작성해주세요.", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
            },
            confirmButton = {
                Text(
                    text = "완료",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF1429A0), shape = RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            // 신고 처리 로직 추가 (예: 서버에 신고 내용 전송)
                            viewModel.onReport()
                            showDialog = false // 다이얼로그 닫기
                        }
                        .padding(10.dp)
                )
            },
            dismissButton = {
                Text(
                    text = "취소",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF1429A0), shape = RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showDialog = false }
                        .padding(10.dp)
                )
            }
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {

        val (topBar, contentBox, hintBox, reportButton) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp, top = 10.dp)
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "학습하기",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "${todaySolvedCount} / ${todayProblems.size}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp)
                .background(color = Color(0xFFFAFAFA))
                .constrainAs(contentBox) {
                    top.linkTo(topBar.bottom, margin = 70.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            val (problemPart, answerPart, submitButton) = createRefs()

            Text(
                text = todayProblems[todaySolvedCount].second,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(problemPart) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            TextField(
                value = answer,
                onValueChange = { viewModel.onAnswerChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                    .constrainAs(answerPart) {
                        top.linkTo(problemPart.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                label = { Text("정답") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )

            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.onSubmit() }
                    .constrainAs(submitButton) {
                        top.linkTo(answerPart.bottom, margin = 10.dp)
                        end.linkTo(parent.end)
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "제출",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .constrainAs(hintBox) {
                    top.linkTo(contentBox.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // `wrongCnt`만큼의 힌트를 표시
            items(wrongCnt) { index ->
                problemHints[todayProblems[0].first]?.getOrNull(index)?.let { hint ->
                    HintCard(baseViewModel, hintWidth, hintHeight, hint, 20, onClick = { baseViewModel.triggerVibration() })
                }
            }
        }

        Box(
          modifier = Modifier
              .width(60.dp)
              .height(30.dp)
              .background(Color.Red, shape = RoundedCornerShape(40.dp))
              .clickable(
                  interactionSource = remember { MutableInteractionSource() },
                  indication = null
              ) {
                  selectedProblem = todayProblems[todaySolvedCount] // 클릭된 문제 설정
                  showDialog = true // 다이얼로그 열기
              }
              .constrainAs(reportButton) {
                  end.linkTo(parent.end, margin = 15.dp)
                  bottom.linkTo(parent.bottom, margin = 15.dp)
              },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "신고",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }

}