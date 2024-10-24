package com.maker.pacemaker.ui.screen.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel

@Composable
fun LoadScreen(baseViewModel: BaseViewModel, mainViewModel: BaseViewModel, viewModel: MainScreenViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (loadText, progressBar) = createRefs()

        Text(
            text = "사용자 정보 등록하는 중...",
            style = TextStyle(
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .constrainAs(loadText) {
                    top.linkTo(parent.top) // 부모의 상단에 연결
                    bottom.linkTo(parent.bottom) // 부모의 하단에 연결
                    start.linkTo(parent.start) // 부모의 왼쪽에 연결
                    end.linkTo(parent.end)
                }
        )

        // Indeterminate ProgressBar
        LinearProgressIndicator(
            modifier = Modifier
                .constrainAs(progressBar) {
                    top.linkTo(loadText.bottom, margin = 16.dp) // 텍스트 아래에 배치
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .fillMaxWidth() // 가로로 최대 너비 설정
                .height(4.dp) // 높이 설정
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadScreenPreview() {
    val baseViewModel = DummyBaseViewModel()
    val loadscreenViewModel = DummyMainScreenViewModel()
    val loadViewModel = DummyMainBaseViewModel()

    LoadScreen(baseViewModel, loadViewModel, loadscreenViewModel)
}
