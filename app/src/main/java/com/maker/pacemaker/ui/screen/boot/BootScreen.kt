package com.maker.pacemaker.ui.screen.boot

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
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
import com.maker.pacemaker.R
import com.maker.pacemaker.data.model.ActivityType
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.screen.Component.UpBar
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.boot.BootBaseViewModel
import com.maker.pacemaker.ui.viewmodel.boot.details.BootScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun BootScreen(viewModel: BootScreenViewModel) {

    val baseViewModel = viewModel.baseViewModel
    val bootViewModel = viewModel.bootViewModel

    val isPermissionGranted by viewModel.isPermissionGranted.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1429A0))
            .clickable(onClick = {
                if (viewModel.fireBaseUID != "") {
                    baseViewModel.goActivity(ActivityType.MAIN)
                } else {
                    baseViewModel.goScreen(ScreenType.ENTRY)
                }
            })
    ) {
        val (starImage, paceText, makerText) = createRefs()

        // 스타 이미지
        Image(
            painter = painterResource(id = R.drawable.logo), // 리소스 확인
            contentDescription = null,
            modifier = Modifier
                .size(150.dp) // 크기 조정
                .constrainAs(starImage) {
                    top.linkTo(parent.top, margin = 200.dp) // 부모 상단에 위치
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // PACE MAKER 텍스트
        Text(
            text = "PACE",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .constrainAs(paceText) {
                    top.linkTo(starImage.bottom, margin = 36.dp) // 스타 이미지 아래에 위치
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "MAKER",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .constrainAs(makerText) {
                    top.linkTo(paceText.bottom, margin = 16.dp) // 스타 이미지 아래에 위치
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        // 권한 결과에 따라 화면 전환
        LaunchedEffect(isPermissionGranted) {
            delay(2000)
            if (isPermissionGranted) {
                if (viewModel.fireBaseUID != "") {
                    baseViewModel.goActivity(ActivityType.MAIN)
                    Log.d("FCM", "UID Exitst: ${viewModel.fireBaseUID}")
                } else {
                    baseViewModel.goScreen(ScreenType.ENTRY)
                    Log.d("FCM", "UID Not Exitst")
                }
            } else {
                // 권한이 거부된 경우 처리 (여기서도 ENTRY 스크린으로 전환 가능)
                baseViewModel.goScreen(ScreenType.ENTRY)
                Log.d("FCM", "Permission Denied")
            }
        }
    }
}