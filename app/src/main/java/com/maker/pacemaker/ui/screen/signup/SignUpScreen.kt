package com.maker.pacemaker.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.signup.details.SignUpScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(viewModel: SignUpScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val signUpBaseViewModel = viewModel.baseViewModel

    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val PWState = remember { mutableStateOf(TextFieldValue("")) }
    val nicknameState = remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val registrationResult by viewModel.registrationResult.observeAsState(null)

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (upBar, divider, titleText,authButton,emailtitleText,emailField,pwtitleText,pwField,nicknameText,nicknameField,signinButton) = createRefs()

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start. linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            Text(
                text = "이메일 인증",
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(screenWidth - 60.dp)
                .height(1.dp)
                .background(Color.Gray)
                .padding(start = 40.dp, end = 40.dp)
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 5.dp)
                }
        )


        Text(
            text = "이메일",
            style = TextStyle(
                fontSize = 15.sp, // 원하는 글씨 크기로 설정
                fontWeight = FontWeight.Light // 선택적으로 글씨 두께를 설정
            ),
            modifier = Modifier
                .constrainAs(emailtitleText) {
                    top.linkTo(upBar.bottom, margin = 32.dp) // 부모의 상단에 연결
                    start.linkTo(parent.start, margin = 25.dp) // 부모의 왼쪽에 연결
                }
            //.padding(bottom = 10.dp) // 텍스트 아래에 여백 추가
        )

        TextField(
            value = emailState.value,
            onValueChange = { newValue -> emailState.value = newValue
                viewModel.checkEmail(emailState.value.text)
            },
            placeholder = { Text("이메일을 입력하세요") },
            visualTransformation = EmailVisualTransformation(), // 시각적 변환 추가
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                cursorColor = Color.Black
            ),
            modifier = Modifier
                .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .constrainAs(emailField) {
                    top.linkTo(emailtitleText.bottom, margin = 0.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
                .padding(3.dp) // 내부 여백 설정
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)) // 배경색과 모서리 둥글기 설정
                .height(56.dp) // 텍스트 필드 높이 설정
                .width(360.dp) // 텍스트 필드 너비 설정
            ,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done// 다음 필드로 이동
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    keyboardController?.hide() // 키패드 숨기기
                }
            )
        )

        if (viewModel.passWordSettingEnabled) {
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
                placeholder = { Text("6자리 이상 비밀번호를 입력하세요") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
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
                ,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next // 다음 필드로 이동
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide() // 키패드 숨기기
                    }
                )
            )

            if (PWState.value.text.length >= 6) {

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

                if (registrationResult?.contains("인증 완료") == true) {

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
                        placeholder = { Text("2 ~ 6글자 닉네임을 입력하세요") },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                            cursorColor = Color.Black
                        ),
                        modifier = Modifier
                            .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
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
                        ,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next // 다음 필드로 이동
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                keyboardController?.hide() // 키패드 숨기기
                            }
                        )
                    )

                    if (nicknameState.value.text.length >= 2 && nicknameState.value.text.length <= 6) {

                        Button(
                            onClick = {
                                viewModel.enrollUserToServer(nicknameState.value.text)
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
            }
        }


    }

}

class EmailVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // 여기에 이메일 형식 변환 로직을 추가
        return TransformedText(text, OffsetMapping.Identity)
    }
}
