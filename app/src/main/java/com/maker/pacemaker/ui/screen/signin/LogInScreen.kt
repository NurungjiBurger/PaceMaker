package com.maker.pacemaker.ui.screen.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummySignInBaseViewModel
import com.maker.pacemaker.data.model.test.DummySignInScreenViewModel
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signin.details.SignInScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(baseViewModel: BaseViewModel, mainViewModel: SignInBaseViewModel, viewModel: SignInScreenViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (upBar, titleText, inputBox, authButton, forgotPassword, googleButton) = createRefs()
        val emailState = remember { mutableStateOf(TextFieldValue("")) }
        val PWState = remember { mutableStateOf(TextFieldValue("")) }
        val keyboardController = LocalSoftwareKeyboardController.current

        // UpBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        ) {
            UpBar(baseViewModel, "LOGIN", false, ActivityType.FINISH, ScreenType.LOAD)
        }

        // Email and Password Fields
        Column(
            modifier = Modifier
                .constrainAs(inputBox) {
                    start.linkTo(upBar.start)
                    end.linkTo(parent.end)
                    top.linkTo(upBar.bottom, margin = 40.dp)
                }
                .fillMaxWidth(0.8f)
                .border(1.dp, Color(0xFFDADADA), shape = RoundedCornerShape(24.dp))
                .height(260.dp)
                .padding(16.dp)

        ) {
            Text(
                text = "Email",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(top = 16.dp) // 상단에 16dp 마진 추가
            )

            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // 회색 테두리와 둥근 모서리 설정
                    .background(Color.White), // 배경색을 흰색으로 설정
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // TextField 배경을 투명으로 설정
                    focusedIndicatorColor = Color.Transparent, // 포커스된 상태의 밑줄을 투명으로 설정
                    unfocusedIndicatorColor = Color.Transparent // 비포커스 상태의 밑줄을 투명으로 설정
                )
            )

            Text(
                text = "Password",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(top = 16.dp) // 상단에 16dp 마진 추가
            )
            TextField(
                value = PWState.value,
                onValueChange = { PWState.value = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // 회색 테두리와 둥근 모서리 설정
                    .background(Color.White), // 배경색을 흰색으로 설정
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // TextField 배경을 투명으로 설정
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        // Sign In Button
        Button(
            onClick = {
                keyboardController?.hide() // 키보드 숨기기
                viewModel.checkUser(emailState.value.text, PWState.value.text)
            },
            modifier = Modifier
                .constrainAs(authButton) {
                    start.linkTo(inputBox.start)
                    end.linkTo(inputBox.end)
                    top.linkTo(inputBox.bottom, margin = 20.dp)
                }
                .fillMaxWidth(0.8f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1429A0))
        ) {
            Text("Sign In", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // Forgot Password Text
        Text(
            text = "Forgot password?",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .constrainAs(forgotPassword) {
                    start.linkTo(authButton.start)
                    top.linkTo(authButton.bottom, margin = 8.dp)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {

    val baseViewModel = DummyBaseViewModel()
    val signinScreenViewModel = DummySignInScreenViewModel()
    val signinViewModel = DummySignInBaseViewModel()

    LogInScreen(baseViewModel, signinViewModel, signinScreenViewModel)
}