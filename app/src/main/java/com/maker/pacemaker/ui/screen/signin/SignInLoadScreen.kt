package com.maker.pacemaker.ui.screen.signin

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
import com.maker.pacemaker.data.model.test.DummySignInBaseViewModel
import com.maker.pacemaker.data.model.test.DummySignInLoadScreenViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.details.SignInScreenViewModel

@Composable
fun SignInLoadScreen(baseViewModel: BaseViewModel, mainViewModel: SignInBaseViewModel, viewModel: SignInScreenViewModel) {
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
fun signInLoadScreenPreview() {
    val baseViewModel = DummyBaseViewModel()
    val signInLoadScreenViewModel = DummySignInLoadScreenViewModel()
    val signInViewModel = DummySignInBaseViewModel()

    SignInLoadScreen(baseViewModel, signInViewModel, signInLoadScreenViewModel)
}
