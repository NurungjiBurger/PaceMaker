package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.screen.Component.BoxCard
import com.maker.pacemaker.ui.screen.Component.ProblemCard
import com.maker.pacemaker.ui.viewmodel.main.details.MainProblemSearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProblemSearchScreen(viewModel: MainProblemSearchScreenViewModel) {

    val words = viewModel.words.collectAsState()
    val hashTags = viewModel.hashTags.collectAsState().value
    val searchedProblems = viewModel.searchedProblems.collectAsState().value

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val searchBoxHeight = 50.dp
    val searchBoxWidth = screenWidth - 40.dp

    val contentBoxHeight = screenHeight / 6
    val contentBoxWidth = screenWidth - 60.dp

    val keyboardController = LocalSoftwareKeyboardController.current

    // 다이얼로그 상태 관리
    var selectedProblem by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // 다이얼로그 열기
    if (showDialog && selectedProblem != null) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = { Text(text = selectedProblem!!.first) },
            text = { Text(text = selectedProblem!!.second) }, // 상세 내용
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        "닫기",
                        color = Color.Black
                    )
                }
            },
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (searchBox, hashTagBox, contentBox) = createRefs()

        ConstraintLayout(
            modifier = Modifier
                .width(searchBoxWidth)
                .height(searchBoxHeight)
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
                .constrainAs(searchBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 50.dp)
                }
        ) {
            val (searchIcon, searchField) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search Icon",
                modifier = Modifier
                    .size(30.dp)
                    .constrainAs(searchIcon) {
                        start.linkTo(parent.start, margin = 10.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

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
                        start.linkTo(searchIcon.end, margin = 30.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 10.dp)
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next // 다음 필드로 이동
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide() // 키패드 숨기기
                    }
                )
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp), // 각 태그가 가로로 80dp 이상이면 줄바꿈
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .constrainAs(hashTagBox) {
                    top.linkTo(searchBox.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(color = Color(0xFFFAFAFA)),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(hashTags.size) { index ->
                val hashTag = hashTags[index]
                Text(
                    text = "#${hashTag}",
                    fontSize = 20.sp,
                    color = Color(0xFFA5A5A5),
                )
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .constrainAs(contentBox) {
                    top.linkTo(hashTagBox.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            items(searchedProblems.size) { index ->
                val problem = searchedProblems[index]
                ProblemCard(problem) {
                    selectedProblem = problem // 클릭된 문제 설정
                    showDialog = true // 다이얼로그 열기
                }
            }
        }
    }

}