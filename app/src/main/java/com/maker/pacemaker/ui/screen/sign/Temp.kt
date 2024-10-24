package com.maker.pacemaker.ui.screen.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.maker.pacemaker.data.model.test.DummyBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainBaseViewModel
import com.maker.pacemaker.data.model.test.DummyMainScreenViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel

//
//@Composable
//fun SignAuthScreen(baseViewModel: BaseViewModel, mainViewModel: BaseViewModel, viewModel: MainScreenViewModel) {
//    val balance by viewModel.balance.collectAsState()
//
//    ConstraintLayout(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color(0xFFF4F2EE))
//    ) {
//        val (titleText,authButton,minititleText,emailField) = createRefs()
//
//    }
//
//}




//@Preview(showBackground = true)
//@Composable
//fun SignAuthScreenPreview() {
//    // Preview용 더미 ViewModel을 생성하여 직접 사용
//
//
//    val baseViewModel = DummyBaseViewModel()
//    val signAuthscreenViewModel = DummyMainScreenViewModel()
//    val signAuthViewModel = DummyMainBaseViewModel()
//
//    // 모든 더미 ViewModel을 전달하여 미리보기 실행
//    SignAuthScreen(baseViewModel, signAuthViewModel, signAuthscreenViewModel)
//}