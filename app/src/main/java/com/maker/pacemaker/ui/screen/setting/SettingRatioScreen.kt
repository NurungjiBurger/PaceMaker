package com.maker.pacemaker.ui.screen.setting

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.BoxCard
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingRatioScreenViewModel


@Composable
fun SettingRatioScreen(viewModel: SettingRatioScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val settingViewModel = viewModel.baseViewModel

    val ratioMode = settingViewModel.ratioMode.collectAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비
    val boxHeight = screenHeight / 7 // 위아래 20.dp씩 빼기
    val boxWidth = ( screenWidth / 5 ) * 4

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        // 레이아웃 구현
        val (dialog, contentBox, selectBox) = createRefs()

        Text(
            text = "문제\n복습 비율을 선택하세요.",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(dialog) {
                    top.linkTo(parent.top, margin = 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(contentBox) {
                    top.linkTo(dialog.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxCard( baseViewModel, boxWidth, boxHeight, "일반 모드", 20,"PaceMaker가 복습 문제와\n새로운 문제의 비율을 알아서 조정해요.", 15,ratioMode.value == "일반 모드", onClick = { viewModel.selectRatioSetting("일반 모드") })

            Spacer(modifier = Modifier.height(20.dp))

            BoxCard( baseViewModel, boxWidth, boxHeight, "복습 모드", 20, "새로운 문제 없이 복습만 해요.", 15,ratioMode.value == "복습 모드", onClick = { viewModel.selectRatioSetting("복습 모드") })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(selectBox) {
                    top.linkTo(contentBox.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(50.dp))
                    .border(1.dp, Color(0xFF000000), shape = RoundedCornerShape(50.dp))
                    .clickable {
                        viewModel.completeRatioSetting(false)
                        baseViewModel.goScreen(ScreenType.MYPAGE)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "취소",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .background(Color(0xFF1429A0), shape = RoundedCornerShape(50.dp))
                    .border(1.dp, Color(0xFF000000), shape = RoundedCornerShape(50.dp))
                    .clickable {
                        viewModel.completeRatioSetting(true)
                        baseViewModel.goScreen(ScreenType.MYPAGE)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "선택완료",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

}

@Composable
@Preview
fun SettingRatioScreenPreview() {

//    val settingRationScreenViewModel: SettingRatioScreenViewModel by viewModels()
//
//    SettingRatioScreen()
}