package com.maker.pacemaker.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainMyPageScreenViewModel
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMyPageScreenViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maker.pacemaker.R
import com.maker.pacemaker.ui.screen.Component.NavCard

@Composable
fun MainMyPageScreen(baseViewModel: BaseViewModel, mainViewModel: MainBaseViewModel, viewModel: MainMyPageScreenViewModel) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, profileBox, contentBox) = createRefs()

        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            if (baseViewModel.previousActivity != ActivityType.FINISH) baseViewModel.previousActivity?.let {
                UpBar(baseViewModel, "내 정보", true,
                    it, ScreenType.FINISH)
            }
            else if (baseViewModel.previousScreen != ScreenType.FINISH) baseViewModel.previousScreen?.let {
                UpBar(baseViewModel, "내 정보", false, ActivityType.FINISH,
                    it
                )
            }
        }

        Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .height(100.dp)
                .constrainAs(profileBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 30.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            // 프로필 사진
            Box(
                modifier = Modifier
                    .size(100.dp) // 크기를 정사각형으로 설정
                    .clip(CircleShape) // Box를 원형으로 만듦
                    .border(1.dp, Color(0xFF1429A0), shape = CircleShape), // 경계선도 원형으로 설정
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape) // 이미지를 원형으로 자름
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "안녕하세요\n호준님:)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(40.dp)
                        .border(1.dp, Color(0xFF1429A0), shape = RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "100XP",
                        fontSize = 15.sp,
                        color = Color(0xFF1429A0)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .background(Color(0xFFD9D9D9))
                .fillMaxWidth()
                .constrainAs(contentBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(profileBox.bottom, margin = 30.dp)
                },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ){
            NavCard(baseViewModel, "1일 학습 목표")
            NavCard(baseViewModel, "퀴즈 카테고리")
            NavCard(baseViewModel, "복습 단어 비율")
        }
    }

}

@Composable
@Preview
fun MainMyPageScreenPreview() {

    val baseViewModel = DummyBaseViewModel()
    val mainViewModel = DummyMainBaseViewModel()
    val viewModel = DummyMainMyPageScreenViewModel()

    MainMyPageScreen(baseViewModel, mainViewModel, viewModel)
}