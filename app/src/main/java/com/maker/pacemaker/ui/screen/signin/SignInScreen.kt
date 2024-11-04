package com.maker.pacemaker.ui.screen.signin

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.activity.main.MainActivity
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.details.SignInScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(viewModel: SignInScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val signInViewModel = viewModel.signInViewModel

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp // 전체 화면 높이
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // 전체 화면 너비

    val context = LocalContext.current // 현재 컨텍스트를 가져옵니다.
    val isLoggedInState = viewModel.isLoggedIn.observeAsState(initial = false) // LiveData를 관찰하여 상태를 가져옵니다.


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (upBar, divider, inputBox, authButton, errorMessageText, forgotPassword) = createRefs()
        val emailState = remember { mutableStateOf(TextFieldValue("")) }
        val PWState = remember { mutableStateOf(TextFieldValue("")) }
        val keyboardController = LocalSoftwareKeyboardController.current

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
                text = "로그인",
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

        // Email and Password Fields
        Column(
            modifier = Modifier
                .constrainAs(inputBox) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth(0.8f)
                .padding(bottom = 30.dp)
        ) {
            TextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                    viewModel.checkEmail(emailState.value.text)
                                },
                label = { Text("Email") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 다음 필드로 이동
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide() // 키패드 숨기기
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = PWState.value,
                onValueChange = { PWState.value = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // 다음 필드로 이동
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide() // 키패드 숨기기
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.coreectEmail && PWState.value.text.length >= 6) {

                // Sign In Button
                Button(
                    onClick = {
                        keyboardController?.hide() // 키보드 숨기기
                        viewModel.checkUser(emailState.value.text, PWState.value.text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1429A0))
                ) {
                    Text("Sign In", color = Color.White)
                }
            }
        }

        // Forgot Password Text
        Text(
            text = "Forgot password?",
            color = Color.Gray,
            modifier = Modifier
                .constrainAs(forgotPassword) {
                    start.linkTo(inputBox.start)
                    top.linkTo(inputBox.bottom, margin = 3.dp)
                }
        )

        // 로그인 상태 변경 관찰
        LaunchedEffect(isLoggedInState.value) {
            if (isLoggedInState.value) {
                // 로그인 성공 시 다른 액티비티로 이동
                val intent = Intent(context, MainActivity::class.java) // 이동할 액티비티로 변경
                context.startActivity(intent)
                (context as? Activity)?.finish() // 현재 액티비티 종료
            }
        }
    }
}