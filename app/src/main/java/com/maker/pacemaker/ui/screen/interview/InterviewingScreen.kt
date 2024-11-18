package com.maker.pacemaker.ui.screen.interview

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.viewmodel.interview.details.InterviewingScreenViewModel

@Composable
fun InterviewingScreen(viewModel: InterviewingScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val interviewViewModel = viewModel.interviewViewModel

    val isLoading by interviewViewModel.isLoading.collectAsState()

    val turn by viewModel.turn.collectAsState()
    val interviews by viewModel.interviews.collectAsState()
    val index by viewModel.index.collectAsState()
    val reAnswerCnt by viewModel.reAnswerCnt.collectAsState()

    val timer by viewModel.timer.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val timerActive by viewModel.timerActive.collectAsState()

    if (isLoading) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFDFE9FE))
        ){
            val (description, progress) = createRefs()

            Text(
                text = "AI가 질문을 생성 중입니다...",
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(progress.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            LinearProgressIndicator(
                color = Color(0xFF14299F),
                modifier = Modifier
                    .fillMaxWidth(0.7f) // 화면 너비의 80%를 채우도록 설정
                    .constrainAs(progress) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

        }

    } else {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFDFE9FE))
        ) {
            val (upBar, divider, description, contentBox) = createRefs()

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
                    text = "CS면접",
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

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

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight - 50.dp)
                    .padding(start = 15.dp, end = 15.dp)
                    .constrainAs(contentBox) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(divider.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(
                    text = if (turn) "답변 녹음 중 ..." else "AI 면접관이 질문 중 ...",
                    fontSize = 20.sp,
                    color = if (turn) Color.Gray else Color.Black,
                )

                Text(
                    text = if (timer < 0) "" else timer.toString(),
                    fontSize = 40.sp,
                    color = Color.Black,
                )

                Image(
                    painter = if (turn) painterResource(id = R.drawable.intervieweroff) else painterResource(id = R.drawable.intervieweron),
                    contentDescription = "Interviewer",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )

                Text(
                    text = interviews[index].question,
                    fontSize = 20.sp,
                    color = if (!turn) Color.Black else Color.Gray,
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = if (turn) painterResource(id = R.drawable.intervieweeon) else painterResource(id = R.drawable.intervieweeoff),
                    contentDescription = "Interviewee",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )

            }

        }
    }
}