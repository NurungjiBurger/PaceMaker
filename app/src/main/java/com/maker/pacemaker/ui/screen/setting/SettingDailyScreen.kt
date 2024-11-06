package com.maker.pacemaker.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.BoxCard

import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingDailyScreenViewModel

@Composable
fun SettingDailyScreen(viewModel: SettingDailyScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val settingViewModel = viewModel.settingViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val dailyCount by baseViewModel.dailyCount.collectAsState()
    val dailySetting by viewModel.dailySetting.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        // 레이아웃 구현
        val (dialog, description, contentBox, selectBox) = createRefs()

        Text(
            text = "많이 한다고 다 좋은 건 아니에요!",
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

        Text(
            text = "자신에게 적당한 하루 목표를\n선택해 보세요.",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(description) {
                    top.linkTo(dialog.bottom, margin = 150.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .constrainAs(contentBox) {
                    top.linkTo(description.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                BoxCard(baseViewModel, 100.dp, 100.dp, "여유롭게", 20,"20개",  30, (dailySetting == "여유롭게"), onClick = { viewModel.selectDailySetting("여유롭게", 20) })
                BoxCard(baseViewModel, 100.dp, 100.dp, "적당하게", 20,"30개", 30, (dailySetting == "적당하게"), onClick = { viewModel.selectDailySetting("적당하게", 30) })
                BoxCard(baseViewModel, 100.dp, 100.dp, "열심히", 20,"50개", 30, (dailySetting == "열심히"), onClick = { viewModel.selectDailySetting("열심히", 50) })
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BoxCard(baseViewModel, 100.dp, 100.dp, "열정적으로", 20,"100개", 30, (dailySetting == "열정적으로"), onClick = {viewModel.selectDailySetting("열정적으로", 100)})

                ConstraintLayout (
                    modifier = Modifier
                        .width(200.dp)
                        .height(100.dp)
                        .background(Color(0xFFFAFAFA))
                        .border(
                            width = if (dailySetting == "나만의 목표") 3.dp else 1.dp, // 조건에 따라 테두리 두께 설정
                            color = if (dailySetting == "나만의 목표") Color(0xFF1429A0) else Color(0xFF000000), // 조건에 따라 테두리 색상 설정
                            shape = RoundedCornerShape(10.dp) // 테두리 모양 설정
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { viewModel.selectDailySetting("나만의 목표", 30) }
                ) {
                    val (titleText, plusButton, minusButton, subTitleText) = createRefs()

                    Text(
                        text = "나만의 목표",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .constrainAs(titleText) {
                                top.linkTo(parent.top, margin = 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    if (dailySetting == "나만의 목표") {

                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1429A0), shape = RoundedCornerShape(50.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.selectDailySetting(
                                        "나만의 목표",
                                        dailyCount - 5
                                    )
                                }
                                .constrainAs(minusButton) {
                                    start.linkTo(parent.start)
                                    end.linkTo(titleText.start)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "-",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1429A0), shape = RoundedCornerShape(50.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.selectDailySetting(
                                        "나만의 목표",
                                        dailyCount + 5
                                    )
                                }
                                .constrainAs(plusButton) {
                                    start.linkTo(titleText.end)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "+",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Text(
                        text = if (dailySetting == "나만의 목표") "${dailyCount}개" else "",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .constrainAs(subTitleText) {
                                top.linkTo(titleText.bottom, margin = 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                }

            }
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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.completeDailySetting(false)
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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.completeDailySetting(true)
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
fun SettingDailyScreenPreview() {

//    val baseViewModel = DummyBaseViewModel()
//    val settingViewModel = DummySettingBaseViewModel()
//    val viewModel = DummySettingDailyScreenViewModel()
//
//    SettingDailyScreen(baseViewModel, settingViewModel, viewModel)
}