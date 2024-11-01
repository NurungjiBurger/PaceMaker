package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val searchBoxHeight = 50.dp
    val searchBoxWidth = screenWidth - 40.dp

    val words = viewModel.words.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val submitedWords by viewModel.submitedWords.collectAsState()

    // 유사도
    val similarity = 55.55

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
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
                append("정답 단어와 가장 유사한 단어의\n유사도는 ")
                withStyle(
                    style = SpanStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                ) {
                    append(similarity.toString())
                }
                append(" 입니다.\n")
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
                        //keyboardController?.hide() // 키패드 숨기기
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
                .height(screenHeight/2)
                .padding(start = 15.dp)
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(contentBoxBorder.bottom        ) // contentBoxBorder 아래에 위치
                    bottom.linkTo(parent.bottom) // 화면의 남은 공간을 채울 수 있도록 설정
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
                        .width(searchBoxWidth)
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(
                        text = "${index}", // 기존 입력 순서 인덱스
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = word.word,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${(word.similarity * 100)}",
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${word.similarityRank}",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}