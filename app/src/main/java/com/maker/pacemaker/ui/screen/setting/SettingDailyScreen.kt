package com.maker.pacemaker.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummySettingBaseViewModel
import com.maker.pacemaker.data.model.test.DummySettingDailyScreenViewModel
import com.maker.pacemaker.ui.screen.Component.BoxCard
import com.maker.pacemaker.ui.screen.Component.BoxCardPreview
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import com.maker.pacemaker.ui.viewmodel.setting.details.SettingDailyScreenViewModel

@Composable
fun SettingDailyScreen(baseViewModel: BaseViewModel, settingViewModel: SettingBaseViewModel, viewModel: SettingDailyScreenViewModel) {

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
                BoxCard(baseViewModel, 100.dp, 100.dp, "여유롭게", "20개", onClick = { settingViewModel.setDailyCount(20) })
                BoxCard(baseViewModel, 100.dp, 100.dp, "적당하게", "30개", onClick = { settingViewModel.setDailyCount(30) })
                BoxCard(baseViewModel, 100.dp, 100.dp, "열심히", "50개", onClick = { settingViewModel.setDailyCount(50) })
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BoxCard(baseViewModel, 100.dp, 100.dp, "열정적으로", "100개", onClick = {settingViewModel.setDailyCount(100)})
                BoxCard(baseViewModel, 225.dp, 100.dp, "나만의 목표", "직접 입력", onClick = {settingViewModel.setDailyCount(0)})
            }
        }
    }

}

@Composable
@Preview
fun SettingDailyScreenPreview() {

    val baseViewModel = DummyBaseViewModel()
    val settingViewModel = DummySettingBaseViewModel()
    val viewModel = DummySettingDailyScreenViewModel()

    SettingDailyScreen(baseViewModel, settingViewModel, viewModel)
}