package com.maker.pacemaker.ui.screen.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel


@Composable
fun SignAuthScreen(baseViewModel: BaseViewModel, mainViewModel: BaseViewModel, viewModel: MainScreenViewModel) {
    val balance by viewModel.balance.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (titleText,authButton,minititleText,emailField) = createRefs()
        val emailState = remember { mutableStateOf(TextFieldValue("")) }

        Text(
            text = "이메일 인증",
            style = TextStyle(
                fontSize = 30.sp, // 원하는 글씨 크기로 설정
                fontWeight = FontWeight.ExtraBold // 선택적으로 글씨 두께를 설정
            ),
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(parent.top, margin = 32.dp) // 부모의 상단에 연결
                    start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                }
                .padding(bottom = 180.dp) // 텍스트 아래에 여백 추가
        )

        Text(
            text = "이메일",
            style = TextStyle(
                fontSize = 18.sp, // 원하는 글씨 크기로 설정
                fontWeight = FontWeight.Light // 선택적으로 글씨 두께를 설정
            ),
            modifier = Modifier
                .constrainAs(minititleText) {
                    top.linkTo(titleText.bottom, margin = 32.dp) // 부모의 상단에 연결
                    start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                }
            //.padding(bottom = 10.dp) // 텍스트 아래에 여백 추가
        )

        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            placeholder = { Text("이메일을 입력하세요") },
            modifier = Modifier
                .constrainAs(emailField) {
                    top.linkTo(minititleText.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .padding(16.dp) // 내부 여백 설정
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)) // 배경색과 모서리 둥글기 설정
                .height(56.dp) // 텍스트 필드 높이 설정
                .width(360.dp) // 텍스트 필드 너비 설정
        )


        Button(
            onClick = {
                baseViewModel.goScreen(ScreenType.TEST)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1429A0), // 버튼 배경색 설정
                contentColor = Color.White // 버튼 텍스트 색상 설정
            ),
            shape = RoundedCornerShape(8.dp), // 모서리 둥글기 설정
            modifier = Modifier
                .constrainAs(authButton) {
                    top.linkTo(emailField.bottom, margin = 16.dp) // 환영 메시지 아래에 배치
                    end.linkTo(parent.end, margin = 16.dp) // 부모의 왼쪽에 배치
                }
                .size(width = 120.dp, height = 50.dp) // 버튼의 크기 설정
        ) {
            Text(
                text = "인증 요청",
                fontSize = 18.sp, // 텍스트 크기 설정
                color = Color.White, // 텍스트 색상 설정
                fontWeight = FontWeight.Bold // 텍스트 두께 설정
            )
        }


    }

}




@Preview(showBackground = true)
@Composable
fun SignAuthScreenPreview() {
    // Preview용 더미 ViewModel을 생성하여 직접 사용


    val baseViewModel = DummyBaseViewModel()
    val signAuthscreenViewModel = DummyMainScreenViewModel()
    val signAuthViewModel = DummyMainBaseViewModel()

    // 모든 더미 ViewModel을 전달하여 미리보기 실행
    SignAuthScreen(baseViewModel, signAuthViewModel, signAuthscreenViewModel)
}