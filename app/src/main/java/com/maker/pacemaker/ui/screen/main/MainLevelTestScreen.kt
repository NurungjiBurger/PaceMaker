package com.maker.pacemaker.ui.screen.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.main.details.MainLevelTestScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLevelTestScreen(viewModel: MainLevelTestScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel.baseViewModel
    val settingViewModel = viewModel.baseViewModel

    val nowProblem = viewModel.nowProblem.collectAsState()
    val userResponse = viewModel.userResponse.collectAsState()

    // Create a mutable state to hold user input
    val textInput = remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAFAFA))
    ) {
        val (upBar, contentBox) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(upBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
        {
            if (baseViewModel.previousActivity != ActivityType.FINISH) baseViewModel.previousActivity?.let {
                Log.d("MainAlarmScreen", "previousActivity: $it")
                UpBar(baseViewModel, "자가 진단", true,
                    it, ScreenType.FINISH)
            }
            else if (baseViewModel.previousScreen != ScreenType.FINISH) baseViewModel.previousScreen?.let {
                Log.d("MainAlarmScreen", "previousScreen: $it")
                UpBar(baseViewModel, "자가 진단", false, ActivityType.FINISH,
                    it
                )
            }
        }

        Column(
            modifier = Modifier
                .constrainAs(contentBox) {
                    top.linkTo(upBar.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "문제 : ${nowProblem.value}",

            )

            TextField(
                value = userResponse.value,
                onValueChange = { viewModel.onUserResponseChange(it) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent, // 비활성 상태에서 밑줄 색상 제거
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ),
                label = {
                    Text(text = "- 없이 입력", color = Color.Black)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next // 다음 필드로 이동
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide() // 키패드 숨기기
                    }
                )
            )
        }
    }

}