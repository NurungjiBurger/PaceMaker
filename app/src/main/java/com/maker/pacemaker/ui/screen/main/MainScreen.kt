package com.maker.pacemaker.ui.screen.main

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.data.model.test.MockApplication
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun MainScreen(baseViewModel: BaseViewModel, mainViewModel: MainBaseViewModel, viewModel: MainScreenViewModel) {

    val balance by viewModel.balance.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        // Constraints 정의
        val (welcomeText, mainButton, balanceText, plusButton) = createRefs()

        // 환영 메시지
        Text(
            text = "Welcome to Main Screen!",
            modifier = Modifier.constrainAs(welcomeText) {
                top.linkTo(parent.top, margin = 32.dp) // 화면 상단에 여백을 두고 배치
                centerHorizontallyTo(parent) // 수평 중앙에 배치
            }
        )

        // 메인 화면으로 이동 버튼
        Button(
            onClick = {
                baseViewModel.goScreen(ScreenType.TEST)
            },
            modifier = Modifier.constrainAs(mainButton) {
                top.linkTo(welcomeText.bottom, margin = 16.dp) // 환영 메시지 아래에 배치
                centerHorizontallyTo(parent) // 수평 중앙에 배치
            }
        ) {
            Text("Go to Main Screen")
        }

        // Balance 텍스트
        Text(
            text = "Main Balance: $balance",
            modifier = Modifier.constrainAs(balanceText) {
                top.linkTo(mainButton.bottom, margin = 16.dp) // 버튼 아래에 배치
                centerHorizontallyTo(parent) // 수평 중앙에 배치
            }
        )

        // Balance 증가 버튼
        Button(
            onClick = { viewModel.plusBalance() },
            modifier = Modifier.constrainAs(plusButton) {
                top.linkTo(balanceText.bottom, margin = 16.dp) // Balance 텍스트 아래에 배치
                centerHorizontallyTo(parent) // 수평 중앙에 배치
            }
        ) {
            Text("Plus Balance")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    // MockApplication 대신 Application 컨텍스트를 직접 전달합니다.
    val dummyBaseViewModel = DummyBaseViewModel()
    val dummyMainScreenViewModel = DummyMainScreenViewModel()
    val dummyMainBaseViewModel = DummyMainBaseViewModel()

    // 모든 더미 ViewModel을 전달하여 미리보기 실행
    MainScreen(dummyBaseViewModel, dummyMainBaseViewModel, dummyMainScreenViewModel)
}