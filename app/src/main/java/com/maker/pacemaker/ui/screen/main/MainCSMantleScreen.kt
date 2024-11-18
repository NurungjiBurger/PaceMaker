package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.viewmodel.main.details.MainCSMantleScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCSMantleScreen(viewModel: MainCSMantleScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val mainViewModel = viewModel.mainViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val searchBoxHeight = 50.dp
    val searchBoxWidth = screenWidth - 40.dp

    val words = viewModel.words.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val showModal by viewModel.showModal.collectAsState()
    val submitedWords by viewModel.submitedWords.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (upBar, divider, description, searchBox, contentDescription, contentBoxBorder, contentBox) = createRefs()

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
                text = "싸맨틀",
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

        Text(
            text = buildAnnotatedString {
                append("단어를 입력해주세요\n ")
                append("유사도를 보며 단어를 유추해주세요\n")
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                ) {
                    append("CS 단어")
                }
                append("만 입력해주세요.")
            },
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(description) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(divider.bottom, margin = 30.dp)
                }
        )

        ConstraintLayout(
            modifier = Modifier
                .width(searchBoxWidth)
                .height(searchBoxHeight)
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
                .constrainAs(searchBox) {
                    top.linkTo(description.bottom, margin = 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            val (searchButton, searchField) = createRefs()

            TextField(
                value = words.value,
                onValueChange = { viewModel.onSearchWordsChanged(it) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                    cursorColor = Color.Black
                ),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center // 텍스트 중앙 정렬
                ),
                modifier = Modifier
                    .constrainAs(searchField) {
                        start.linkTo(parent.start, margin = 10.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next // 다음 필드로 이동
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        viewModel.onSearchButtonClicked()
                    }
                )
            )

            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search Icon",
                modifier = Modifier
                    .size(30.dp)
                    .constrainAs(searchButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 10.dp)
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.onSearchButtonClicked() },
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(searchBoxWidth)
                .padding(start = 20.dp, end = 20.dp)
                .constrainAs(contentDescription) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(searchBox.bottom, margin = 30.dp)
                }
        ) {
            // # // 추측한 단어 // 유사도 // 유사도 순위
            Text(
                text = "#",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "추측한 단어",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "유사도",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "유사도 순위",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Box(
            modifier = Modifier
                .width(searchBoxWidth)
                .height(2.dp)
                .background(Color.Black)
                .constrainAs(contentBoxBorder) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(contentDescription.bottom, margin = 5.dp)
                }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 2)
                .padding(start = 15.dp)
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(contentBoxBorder.bottom)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            // 유사도 순으로 내림차순 정렬
            val sortedWords = submitedWords.sortedBy { it.similarityRank }

            items(sortedWords.size) { index ->

                val word = sortedWords[index]

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .height(IntrinsicSize.Min) // 텍스트 길이에 따라 최소 높이 설정
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(30.dp)
                    )

                    Text(
                        text = word.word,
                        fontSize = 18.sp,
                        maxLines = 2, // 최대 줄 수 제한
                        softWrap = true, // 텍스트 줄바꿈 허용
                        modifier = Modifier
                            .width(IntrinsicSize.Max) // 텍스트 길이에 맞춰 확장
                            .weight(1f) // Row에서 남은 공간 사용
                            .padding(start = 8.dp) // 유사도 텍스트와 간격
                    )

                    Text(
                        text = "${word.similarity}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .width(100.dp) // 필요한 경우 너비 조절
                            .padding(end = 40.dp) // 텍스트를 왼쪽으로 이동시키기 위해 끝 쪽에 패딩 추가
                            .alignBy { it.measuredHeight / 2 } // 부모 기준 위치 정렬
                    )

                    Text(
                        text = "${word.similarityRank}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(end = 52.dp)
                            .alignBy { it.measuredHeight / 2 } // 부모 기준 위치 정렬
                    )
                }
            }
        }
    }

    if (showModal) {
        AlertDialog(
            onDismissRequest = { viewModel.hideModal() },
            title = { Text("정답!") },
            text = { Text("축하합니다! 정답을 맞추셨습니다.") },
            confirmButton = {
                TextButton(onClick = { viewModel.hideModal() }) {
                    Text("확인")
                }
            },
            containerColor = Color.White
        )
    }
}