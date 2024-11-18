package com.maker.pacemaker.ui.screen.interview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.interview.details.InterviewStartScreenViewModel

@Composable
fun InterviewStartScreen(viewModel: InterviewStartScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val interviewViewModel = viewModel.interviewViewModel

    val text by interviewViewModel.text.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
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

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight - 100.dp)
                .padding(start = 15.dp, end = 15.dp)
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(divider.bottom)
                    bottom.linkTo(parent.bottom)
                }
        ){
            Text(
                text = "자기소개서를 제출해주세요.\n자기소개서 바탕의 CS면접이 이루어집니다.",
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                val (inputField, description) = createRefs()

                TextField(
                    modifier = Modifier
                        .constrainAs(inputField) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .fillMaxWidth()
                        .height(280.dp),
                    value = text,
                    onValueChange = { viewModel.onTextChanged(it) },
                    label = { Text("자기소개서 내용을 입력해주세요.", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Text(
                    text = "${text.length}/3000자",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .constrainAs(description) {
                            end.linkTo(inputField.end)
                            top.linkTo(inputField.bottom, margin = 5.dp)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp)
                        .background(Color(0xFF14299F), shape = RoundedCornerShape(10.dp))
                        .padding(5.dp)
                        .clickable {
                            if (text.length > 350) viewModel.onSubmit()
                            else viewModel.canNotSubmit()
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "제출",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp)
                        .background(Color(0xFF14299F), shape = RoundedCornerShape(10.dp))
                        .padding(5.dp)
                        .clickable {
                            viewModel.onResult()
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "결과",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }

    }

}