package com.maker.pacemaker.ui.screen.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.screen.Component.HintCard
import com.maker.pacemaker.ui.screen.Component.Loading
import com.maker.pacemaker.ui.screen.Component.ProblemCard
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSearchScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSolveScreenViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainProblemSolveScreen(viewModel: MainProblemSolveScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val mainViewModel = viewModel.mainViewModel

    val isLoading by baseViewModel.isLoading.collectAsState()
    val allQuizSolved by viewModel.allQuizSolved.collectAsState()

    val nowProblemIndex by viewModel.nowProblemIndex.collectAsState()
    val todaySolvedCount by viewModel.todaySolvedCount.collectAsState()
    val todayProblems by viewModel.todayProblems.collectAsState()
    val todayWrongCount by viewModel.todayWrongCount.collectAsState()
    val problemHints by viewModel.problemHints.collectAsState()
    val answer by viewModel.answer.collectAsState()
    val wrongCnt by viewModel.wrongCnt.collectAsState()
    val report by viewModel.report.collectAsState()
    //val allSolved by viewModel.mainViewModel.allQuizSolved.collectAsState()


    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val hintWidth = screenWidth - 60.dp
    val hintHeight = screenHeight / 9

    val keyboardController = LocalSoftwareKeyboardController.current

    val solved = baseViewModel.allQuizSolved.value
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


    // 다이얼로그 상태 관리
    var selectedProblem by remember { mutableStateOf<Problem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // 다이얼로그 열기
    if (showDialog && selectedProblem != null) {

        // word 필드에서 answer JSON 문자열을 추출합니다.
        val wordJson = selectedProblem?.word

        // Gson을 사용하여 JSON 파싱을 수행합니다.
        val gson = Gson()
        val jsonObject = gson.fromJson(wordJson, JsonObject::class.java)

        // answer 배열을 가져오고 첫 번째 답변을 추출합니다.
        val firstAnswer = jsonObject.getAsJsonArray("answer").firstOrNull()?.asString ?: "정답이 없습니다."

        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = { Text(text = firstAnswer) },
            text = { Text(text = selectedProblem!!.description) }, // 상세 내용
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent
                    ),
                ) {
                    Text(
                        "닫기",
                        color = Color.Black
                    )
                }
            },
        )
    }

    //  힌트 다이얼로그 상태 관리
    var selectedProblemHintTitle by remember { mutableStateOf<String?>(null) }
    var selectedProblemHint by remember { mutableStateOf<String?>(null) }
    var showHintDialog by remember { mutableStateOf(false) }

    if (showHintDialog && selectedProblemHint != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showHintDialog = false },
            title = { Text(text = selectedProblemHintTitle!!) },
            text = { Text(text = selectedProblemHint!!) }, // 상세 내용
            confirmButton = {
                TextButton(
                    onClick = { showHintDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent
                    ),
                ) {
                    Text(
                        "닫기",
                        color = Color.Black
                    )
                }
            },
        )
    }

    // 신고 다이얼로그 상태 관리
    var selectedReportProblem by remember { mutableStateOf<Problem?>(null) }
    var showReportDialog by remember { mutableStateOf(false) }

    if (showReportDialog && selectedReportProblem != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showReportDialog = false }, // 다이얼로그 외부 클릭 시 닫기
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
                            showReportDialog = false // 다이얼로그 닫기
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
                        ) { showReportDialog = false }
                        .padding(10.dp)
                )
            }
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (topBar, contentBox, hintBox, reportButton) = createRefs()

        Box(
            modifier = Modifier
                .width(screenWidth - 60.dp)
                .height(70.dp)
                .background(Color(0xFFEFF4FE), shape = RoundedCornerShape(10.dp))
                .constrainAs(topBar) {
                    top.linkTo(parent.top, margin = 40.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width((screenWidth - 60.dp) / 3)
                        .height(50.dp)
                ) {
                    Text(
                        text = "전체 문제",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "${todayProblems.size}개",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color.Gray)
                )

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width((screenWidth - 60.dp) / 3)
                        .height(50.dp)
                ) {
                    Text(
                        text = "맞춘 문제",
                        fontSize = 20.sp,
                        color = Color(0xFF5387F7),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "${todaySolvedCount}개",
                        fontSize = 20.sp,
                        color = Color(0xFF5387F7),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color.Gray)
                )

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width((screenWidth - 60.dp) / 3)
                        .height(50.dp)
                ) {
                    Text(
                        text = "틀린 문제",
                        fontSize = 20.sp,
                        color = Color(0xFFEC5151),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "${todayWrongCount}개",
                        fontSize = 20.sp,
                        color = Color(0xFFEC5151),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        if (todayProblems.size == nowProblemIndex && todayProblems.isNotEmpty()) {
            baseViewModel.setAllQuizSolved(true)

            Log.d("MainProblemSolveScreen", "todayProblems: $todayProblems")

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2)
                    .padding(30.dp)
                    .constrainAs(contentBox) {
                        top.linkTo(topBar.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    // 네비게이션 바 아래 여백을 추가
                    .padding(bottom = WindowInsets.navigationBars.getTop(density = LocalDensity.current).dp),
            ) {
                items(todayProblems.size) { index ->
                    val problem = todayProblems[index]
                    Log.d("MainProblemSolveScreen", "problem: $problem")
                    ProblemCard(problem) {
                        selectedProblem = problem // 클릭된 문제 설정
                        showDialog = true // 다이얼로그 열기
                    }
                }
            }

        } else {
            ConstraintLayout(
                modifier = Modifier
                    .padding(20.dp)
                    .background(color = Color(0xFFDFE9FE))
                    .constrainAs(contentBox) {
                        top.linkTo(topBar.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                val (problemPart, answerPart, skipButton, submitButton) = createRefs()

                if (!todayProblems.isEmpty()) {
                    Text(
                        text = todayProblems[nowProblemIndex].description,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.constrainAs(problemPart) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }

                TextField(
                    value = answer,
                    onValueChange = { viewModel.onAnswerChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, shape = RoundedCornerShape(10.dp))
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
                ) {
                    Text(
                        text = "제출",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Log.d("MainProblemSolveScreen", "wrongCnt: $wrongCnt")

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .background(Color(0xFFFFCC5D), shape = RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (wrongCnt >= 3) viewModel.onSkip()
                        }
                        .constrainAs(skipButton) {
                            top.linkTo(answerPart.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =  if (wrongCnt >= 3) "넘기기" else "${wrongCnt} / 3" ,
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
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // `wrongCnt`만큼의 힌트를 표시
                items(wrongCnt) { index ->
                    problemHints[todayProblems[nowProblemIndex].problem_id]?.getOrNull(index)
                        ?.let { hint ->
                            HintCard(
                                baseViewModel,
                                hintWidth,
                                hintHeight,
                                "힌트 ${index + 1}",
                                20,
                                onClick = {
                                    selectedProblemHintTitle = "힌트 ${index + 1}" // 클릭된 힌트 인덱스 설정
                                    selectedProblemHint = hint // 클릭된 문제 설정
                                    showHintDialog = true // 다이얼로그 열기
                                })
                        }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        selectedReportProblem = todayProblems[nowProblemIndex] // 클릭된 문제 설정
                        showReportDialog = true // 다이얼로그 열기
                    }
                    .constrainAs(reportButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "> 신고하기",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFFF1111),
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
            }

            // 로딩 다이얼로그
            isLoading?.let {
                Loading(
                    "학습문제 가져오는 중...",
                    isLoading = it,
                    onDismiss = { /* Dismiss Logic */ })
            }
        }
    }
}