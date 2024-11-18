package com.maker.pacemaker.ui.screen.main

import android.app.Application
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.ui.screen.Component.BottomNavBar
import com.maker.pacemaker.ui.screen.Component.Loading
import com.maker.pacemaker.ui.screen.Component.Logo
import com.maker.pacemaker.ui.screen.Component.Rating
import com.maker.pacemaker.ui.screen.Component.TopNavBar

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val user by baseViewModel.userInfo.collectAsState()
    val isLoading by baseViewModel.isLoading.collectAsState()
    val allSolved by baseViewModel.allQuizSolved.collectAsState()
    val allCSMantleSolved by baseViewModel.CSMantleSolved.collectAsState()
    val context = LocalContext.current
    // 레벨 값을 1에서 6 사이로 보정
    val correctedLevel = when {
        user.level < 1 -> 1
        user.level > 6 -> 6
        else -> user.level
    }

    val resourceId = context.resources.getIdentifier(
        "level_$correctedLevel", "drawable", context.packageName
    )

    LaunchedEffect(Unit) {
        Log.d("MainScreen", "allSolved initial value: $allSolved")
        val currentCSMantleSolved = baseViewModel.sharedPreferences.getBoolean("CSMantleSolved", false)
        Log.d("MainProblemSolveScreenViewModel", "CSMantleSolved 값: $allCSMantleSolved")


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.mainbg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (upBar, greeting, rating, contentBox, bottomBar) = createRefs()

            // 상단바
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // 너비를 90%로 설정
                    .height(60.dp)
                    .constrainAs(upBar) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(parent.start) // 수평 중앙에 배치
                        end.linkTo(parent.end)
                    }
            ) {
                TopNavBar(baseViewModel)
            }

            // 환영 메시지와 사용자 이름
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // 너비를 90%로 설정
                    .padding(horizontal = 16.dp)
                    .constrainAs(greeting) {
                        top.linkTo(upBar.bottom, margin = 25.dp)
                        start.linkTo(parent.start) // 수평 중앙에 배치
                        end.linkTo(parent.end)
                    },
                horizontalAlignment = Alignment.Start
            ) {
                // 첫 번째 Row: "안녕하세요 [닉네임]"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "안녕하세요",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 텍스트 간 여백
                    Text(
                        text = "${user.nickname}님",
                        fontSize = 29.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f)) // 오른쪽 정렬용
                    // XP 표시
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50)) // 동그란 모양
                            .background(Color(0xFFE3E7FF)) // 배경색
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${user.exp}XP",
                            fontSize = 16.sp,
                            color = Color(0xFF2B59C3),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // 두 번째 Row: "오늘도 재밌는 퀴즈 풀어요~" + 모자 아이콘
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "오늘도 재밌는 퀴즈 풀어요~",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(65.dp)) // 텍스트와 아이콘 간 여백
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }

            // 콘텐츠 Box 영역
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // 너비를 90%로 설정
                    .padding(horizontal = 16.dp)
                    .constrainAs(contentBox) {
                        top.linkTo(greeting.bottom, margin = 16.dp)
                        bottom.linkTo(bottomBar.top, margin = 16.dp)
                        start.linkTo(parent.start) // 수평 중앙에 배치
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.spacedBy(25.dp),
                contentPadding = PaddingValues(vertical = 16.dp) // 상하단 패딩 설정
            ) {
                // "오늘의 학습" Box
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (allSolved) Color(0xFF737CAE) else Color(0xFF1429A0))
                            .clickable { baseViewModel.goScreen(ScreenType.PROBLEMSOLVE) },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = if (allSolved) R.drawable.lightoff1 else R.drawable.light1),
                                contentDescription = null,
                                modifier = Modifier.size(55.dp)
                            )
                            Text(
                                text = "오늘의 학습",
                                color = if (allSolved) Color(0xFFE2E2E2) else Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Image(
                                painter = painterResource(id = if (allSolved) R.drawable.lightoff2 else R.drawable.light2),
                                contentDescription = null,
                                modifier = Modifier.size(55.dp)
                            )
                        }
                    }
                }

                // "CS 면접" Box

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFDFE9FE))
                            .clickable { baseViewModel.goActivity(ActivityType.INTERVIEW) },
                        contentAlignment = Alignment.Center

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 35.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.interviewer1),
                                contentDescription = "Icon 1",
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "CS 면접",
                                color = Color(0xFF1429A0),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.interviewer2),
                                contentDescription = "Icon 2",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }

                // "싸 맨 틀" Box
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (allCSMantleSolved) Color(0xFF737CAE) else Color(
                                    0xFF5387F7
                                ).copy(alpha = 0.3f)
                            )
                            .clickable {
                                if (allCSMantleSolved) baseViewModel.goScreen(ScreenType.CSRANKING)
                                else baseViewModel.goScreen(ScreenType.CSMANTLE)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "싸 맨 틀",
                            color = if (allCSMantleSolved) Color(0xFFE2E2E2) else Color(0xFF1429A0),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
             // 하단바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                BottomNavBar(baseViewModel)
            }
        }
    }

    // 로딩 다이얼로그
    isLoading?.let { Loading("로딩 중...", isLoading = it, onDismiss = { /* Dismiss Logic */ }) }
}
