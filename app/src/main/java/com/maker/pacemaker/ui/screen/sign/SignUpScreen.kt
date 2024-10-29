package com.maker.pacemaker.ui.screen.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummySignBaseViewModel
import com.maker.pacemaker.data.model.test.DummySignUpScreenViewModel
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.sign.SignBaseViewModel
import com.maker.pacemaker.ui.viewmodel.sign.details.SignUpScreenViewModel

@Composable
fun SignUpScreen(baseViewModel: BaseViewModel, mainViewModel: SignBaseViewModel, viewModel: SignUpScreenViewModel) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (upBar,titleText,authButton,emailtitleText,emailField,pwtitleText,pwField,nicknameText,nicknameField,signinButton) = createRefs()
        val emailState = remember { mutableStateOf(TextFieldValue("")) }
        val PWState = remember { mutableStateOf(TextFieldValue("")) }
        val nicknameState = remember { mutableStateOf(TextFieldValue("")) }
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start. linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }

        )
        {
            //if (baseViewModel.previousScreenType == ScreenType.LOAD)
            UpBar(baseViewModel, "", false, ActivityType.FINISH, ScreenType.SIGNUPLOAD)
        }

        Text(
            text = "이메일 인증",
            style = TextStyle(
                fontSize = 30.sp, // 원하는 글씨 크기로 설정
                fontWeight = FontWeight.ExtraBold // 선택적으로 글씨 두께를 설정
            ),
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(upBar.top, margin = 32.dp) // 부모의 상단에 연결
                    start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                }
                .padding(top = 30.dp)
                .padding(bottom = 70.dp) // 텍스트 아래에 여백 추가
        )

        Text(
            text = "이메일",
            style = TextStyle(
                fontSize = 15.sp, // 원하는 글씨 크기로 설정
                fontWeight = FontWeight.Light // 선택적으로 글씨 두께를 설정
            ),
            modifier = Modifier
                .constrainAs(emailtitleText) {
                    top.linkTo(titleText.bottom, margin = 32.dp) // 부모의 상단에 연결
                    start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                }
            //.padding(bottom = 10.dp) // 텍스트 아래에 여백 추가
        )

        TextField(
            value = emailState.value,
            onValueChange = { newValue ->
                emailState.value = newValue
            },
            placeholder = { Text("이메일을 입력하세요") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = EmailVisualTransformation(), // 시각적 변환 추가
            modifier = Modifier
                .constrainAs(emailField) {
                    top.linkTo(emailtitleText.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .padding(3.dp) // 내부 여백 설정
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)) // 배경색과 모서리 둥글기 설정
                .height(56.dp) // 텍스트 필드 높이 설정
                .width(360.dp) // 텍스트 필드 너비 설정
        )

        if (emailState.value.text.length >= 10) {
            Text(
                text = "비밀번호",
                style = TextStyle(
                    fontSize = 15.sp, // 원하는 글씨 크기로 설정
                    fontWeight = FontWeight.Light // 선택적으로 글씨 두께를 설정
                ),
                modifier = Modifier
                    .constrainAs(pwtitleText) {
                        top.linkTo(emailField.bottom, margin = 0.dp) // 부모의 상단에 연결
                        start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                    }
                //.padding(bottom = 10.dp) // 텍스트 아래에 여백 추가
            )

            TextField(
                value = PWState.value,
                onValueChange = { PWState.value = it },
                placeholder = { Text("비밀번호를 입력하세요") },
                modifier = Modifier
                    .constrainAs(pwField) {
                        top.linkTo(pwtitleText.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .padding(3.dp) // 내부 여백 설정
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ) // 배경색과 모서리 둥글기 설정
                    .height(56.dp) // 텍스트 필드 높이 설정
                    .width(360.dp) // 텍스트 필드 너비 설정
            )
            Button(
                onClick = {
                    //val context = LocalContext.current

                    keyboardController?.hide() // 키보드 숨기기
                    viewModel.checkUser(emailState.value.text, PWState.value.text)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1429A0), // 버튼 배경색 설정
                    contentColor = Color.White // 버튼 텍스트 색상 설정
                ),
                shape = RoundedCornerShape(8.dp), // 모서리 둥글기 설정
                modifier = Modifier
                    .constrainAs(authButton) {
                        top.linkTo(pwField.bottom, margin = 16.dp) // 환영 메시지 아래에 배치
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



            Text(
                text = "닉네임",
                style = TextStyle(
                    fontSize = 15.sp, // 원하는 글씨 크기로 설정
                    fontWeight = FontWeight.Light // 선택적으로 글씨 두께를 설정
                ),
                modifier = Modifier
                    .constrainAs(nicknameText) {
                        top.linkTo(authButton.bottom, margin = 32.dp) // 부모의 상단에 연결
                        start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                    }
                //.padding(bottom = 10.dp) // 텍스트 아래에 여백 추가
            )

            TextField(
                value = nicknameState.value,
                onValueChange = { nicknameState.value = it },
                placeholder = { Text("닉네임을 입력하세요") },
                modifier = Modifier
                    .constrainAs(nicknameField) {
                        top.linkTo(nicknameText.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .padding(3.dp) // 내부 여백 설정
                    .background(
                        Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ) // 배경색과 모서리 둥글기 설정
                    .height(56.dp) // 텍스트 필드 높이 설정
                    .width(360.dp) // 텍스트 필드 너비 설정
            )
        }
        Button(
            onClick = {
                // 이메일 인증 요청
                // 이메일 주소를 가져와서 인증 메일을 보내는 기능을 구현
                // 이메일 주소는 emailState.value.text로 가져올 수 있음
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1429A0), // 버튼 배경색 설정
                contentColor = Color.White // 버튼 텍스트 색상 설정
            ),
            shape = RoundedCornerShape(8.dp), // 모서리 둥글기 설정
            modifier = Modifier
                .constrainAs(signinButton) {

                    bottom.linkTo(parent.bottom, margin = 16.dp) // 환영 메시지 아래에 배치
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp) // 부모의 왼쪽에 배치
                }
                .size(width = 360.dp, height = 50.dp) // 버튼의 크기 설정
        ) {
            Text(
                text = "로그인 / 회원가입",
                fontSize = 18.sp, // 텍스트 크기 설정
                color = Color.White, // 텍스트 색상 설정
                fontWeight = FontWeight.Bold // 텍스트 두께 설정
            )
        }


    }

}

class EmailVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 여기에 이메일 형식 변환 로직을 추가
        return TransformedText(text, OffsetMapping.Identity)
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    // Preview용 더미 ViewModel을 생성하여 직접 사용


    val baseViewModel = DummyBaseViewModel()
    val signupScreenViewModel = DummySignUpScreenViewModel()
    val signViewModel = DummySignBaseViewModel()

    // 모든 더미 ViewModel을 전달하여 미리보기 실행
    SignUpScreen(baseViewModel, signViewModel, signupScreenViewModel)
}
