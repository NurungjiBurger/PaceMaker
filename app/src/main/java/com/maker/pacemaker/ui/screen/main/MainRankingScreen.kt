package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.remote.SearchUser
import com.maker.pacemaker.ui.screen.Component.UserCard
import com.maker.pacemaker.ui.viewmodel.main.details.MainRankingScreenViewModel

@Composable
fun MainRankingScreen(viewModel: MainRankingScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val userName = viewModel.userName.collectAsState()
    val userList = viewModel.userList.collectAsState().value

    val userCardWidth = screenWidth - 60.dp
    val userCardHeight = screenHeight / 7

    // 다이얼로그 상태 관리
    var selectedUser by remember { mutableStateOf<SearchUser?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFDFE9FE))
    ) {
        val (medalImage, rankingTitle, userListBox) = createRefs()

        // 메달 이미지
        Image(
            painter = painterResource(id = R.drawable.medal), // 메달 이미지 리소스
            contentDescription = "Medal",
            modifier = Modifier
                .size(120.dp)
                .constrainAs(medalImage) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // 타이틀 행 (등수, 닉네임, 경험치)
        Row(
            modifier = Modifier
                .width(380.dp)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .background(Color(0xFFF0F4FF), shape = RoundedCornerShape(10.dp))
                .padding(8.dp)
                .constrainAs(rankingTitle) {
                    top.linkTo(medalImage.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "등수",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Text(
                text = "닉네임",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Text(
                text = "경험치",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }

        // 사용자 랭킹 리스트
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .width(340.dp)
                .height(550.dp)
                .background(color = Color(0xFFFFFFFF),shape = RoundedCornerShape(10.dp))
                //.padding(horizontal = 50.dp)
                .constrainAs(userListBox) {
                    top.linkTo(rankingTitle.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            itemsIndexed(userList) { index, user ->
                Row {
                    Text(
                        text = "${index + 1}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold, // 최대한 굵게 설정
                        fontStyle = FontStyle.Italic,
                        //fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 15.dp, top = 8.dp)
                    )
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
}
