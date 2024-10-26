package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {

        val (topBar, contentBox, hintBox, reportButton) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp)
                .constrainAs(topBar) {
                    top.linkTo(parent.top, margin = 10.dp)
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
                .padding(start = 10.dp, end = 10.dp)
                .constrainAs(contentBox) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(hintBox.top)
                }
        ) {
            val (problem, answer, wrongCnt, hint) = createRefs()

            Text(
                text = todayProblems[0].second,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(problem) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )



            Text(
                text = "힌트",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(hint) {
                    top.linkTo(wrongCnt.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = problemHints[todayProblems[0].first]?.joinToString("\n") ?: "",
                fontSize = 20.sp,
                modifier = Modifier.constrainAs(answer) {
                    top.linkTo(hint.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }

    }

}