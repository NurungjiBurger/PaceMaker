package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import androidx.constraintlayout.compose.Dimension
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.User
import com.maker.pacemaker.ui.screen.Component.AlarmBox
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.screen.Component.UserCard
import com.maker.pacemaker.ui.viewmodel.main.details.MainRankingScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRankingScreen(viewModel: MainRankingScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val mainViewModel = viewModel.baseViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val searchBoxHeight = 50.dp
    val searchBoxWidth = screenWidth - 40.dp

    val userCardWidth = screenWidth - 60.dp
    val userCardHeight = screenHeight / 7

    val keyboardController = LocalSoftwareKeyboardController.current

    val userName = viewModel.userName.collectAsState()
    val userList = viewModel.userList.collectAsState().value

    // 다이얼로그 상태 관리
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // 다이얼로그 열기
    if (showDialog && selectedUser != null) {
        val isFolloing = selectedUser!!.isFollowing
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = {
                UserCard(
                    baseViewModel = baseViewModel,
                    width = userCardWidth,
                    height = userCardHeight,
                    user = selectedUser!!,
                    onClick = { showDialog = false }, // 다이얼로그 내부에서는 클릭 이벤트 불필요
                    followToggle = { viewModel.toggleFollow(selectedUser!!) }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.toggleFollow(selectedUser!!)
                        // updated user 정보를 selectedUser에 다시 설정하여 상태를 반영
                        selectedUser = selectedUser!!.copy(isFollowing = !selectedUser!!.isFollowing)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent
                    ),
                    ) {
                        Text(
                            text = if (selectedUser!!.isFollowing) "unfollow" else "follow",
                            color = Color.Black
                        )
                    }
                }
            )
        }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, divider, searchBox, userListBox) = createRefs()

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
                text = "랭킹",
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

        ConstraintLayout(
            modifier = Modifier
                .width(searchBoxWidth)
                .height(searchBoxHeight)
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFFD9D9D9), shape = RoundedCornerShape(10.dp))
                .constrainAs(searchBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 20.dp)
                }
        ) {
            val (searchButton, searchField) = createRefs()

            TextField(
                value = userName.value,
                onValueChange = { viewModel.onUserNameChanged(it) },
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
                        keyboardController?.hide() // 키패드 숨기기
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

        // 유저 리스트 표시 영역
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(userListBox) {
                    top.linkTo(searchBox.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(30.dp)
        ) {
            // itemsIndexed를 사용하여 각 아이템의 인덱스 접근
            itemsIndexed(userList) { index, user ->
                UserCard(
                    baseViewModel,
                    userCardWidth,
                    userCardHeight,
                    user,
                    onClick = {
                        selectedUser = user
                        showDialog = true
                    }
                )
            }
        }

    }

}