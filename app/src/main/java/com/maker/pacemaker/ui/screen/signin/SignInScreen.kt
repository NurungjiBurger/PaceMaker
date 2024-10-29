package com.maker.pacemaker.ui.screen.signin

import android.app.Activity
import android.content.Intent
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val signInBaseViewModel = viewModel.baseViewModel

    val context = LocalContext.current // 현재 컨텍스트를 가져옵니다.
    val isLoggedInState = viewModel.isLoggedIn.observeAsState(initial = false) // LiveData를 관찰하여 상태를 가져옵니다.


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF4F2EE))
    ) {
        val (upBar, inputBox, authButton, errorMessageText, forgotPassword) = createRefs()
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
            UpBar(baseViewModel, "LOGIN", false, ActivityType.FINISH, ScreenType.SIGNINLOAD)
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
            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            TextField(
                value = PWState.value,
                onValueChange = { PWState.value = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
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
            Text("Sign In", color = Color.White)
        }

        // Error Message
        viewModel.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier
                    .constrainAs(errorMessageText) {
                        start.linkTo(authButton.start)
                        top.linkTo(authButton.bottom, margin = 8.dp)
                    }
                    .padding(16.dp)
            )
        }

        // 로그인 상태 변경 관찰
        LaunchedEffect(isLoggedInState.value) {
            if (isLoggedInState.value) {
                // 로그인 성공 시 다른 액티비티로 이동
                val intent = Intent(context, MainActivity::class.java) // 이동할 액티비티로 변경
                context.startActivity(intent)
                (context as? Activity)?.finish() // 현재 액티비티 종료
            }
        }


        // Forgot Password Text
        Text(
            text = "Forgot password?",
            color = Color.Gray,
            modifier = Modifier
                .constrainAs(forgotPassword) {
                    start.linkTo(authButton.start)
                    top.linkTo(authButton.bottom, margin = 8.dp)
                }
        )
    }
}