package com.maker.pacemaker.ui.screen.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.remote.Interview
import com.maker.pacemaker.data.model.remote.sendCVResponse
import com.maker.pacemaker.ui.screen.Component.InterviewResultCard
import com.maker.pacemaker.ui.screen.Component.InterviewResultDialog
import com.maker.pacemaker.ui.viewmodel.interview.details.InterviewResultScreenViewModel

@Composable
fun InterviewResultScreen(viewModel: InterviewResultScreenViewModel) {
    val baseViewModel = viewModel.baseViewModel
    val interviewViewModel = viewModel.interviewViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (upBar, divider, contentBox) = createRefs()
        
    val interviewing by interviewViewModel.interviewing.collectAsState()
    val interviewResults by viewModel.interviewResults.collectAsState()
    val interviews by viewModel.interviews.collectAsState()

    val isLoading by viewModel.interviewViewModel.isLoading.collectAsState()
    val selectedInterviewResult = remember { mutableStateOf<sendCVResponse?>(null) }
    val selectedInterview = remember { mutableStateOf<Interview?>(null) }

    if (isLoading) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFAFAFA))
        ) {
            val (description, progress) = createRefs()

            Text(
                text = "면접 결과를 받아오는 중입니다...",
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
                .background(color = Color(0xFFFAFAFA))
        ) {
            val (upBar, divider, contentBox) = createRefs()

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

            LazyColumn(
                modifier = Modifier
                    .height(screenHeight * 4 / 5)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(contentBox){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(divider.bottom, margin = 20.dp)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                items(interviewResults.size) { index ->
                    InterviewResultCard(interviewResult = interviewResults[index]) { selectedResult ->
                        selectedInterviewResult.value = selectedResult
                    }
                }
            }

            // 다이얼로그 처리: 면접 결과 다이얼로그
            selectedInterviewResult.value?.let { interviewResult ->
                interviews[interviewResult.cv_id]?.let {
                    InterviewResultDialog(interviewResult, it, screenWidth * 2 / 3, screenHeight * 4 / 5) {
                        // 다이얼로그를 닫기
                        selectedInterviewResult.value = null
                    }
                }

            }
        }
    }
}
