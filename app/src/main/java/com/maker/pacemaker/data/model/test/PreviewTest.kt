package com.maker.pacemaker.data.model.test

import android.app.Application
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel

class PreviewTest {
}

// 모의 Application 클래스
class MockApplication : Application()

// 미리보기를 위한 더미 ViewModel
class DummyBaseViewModel(application: Application) : BaseViewModel(application) {
    fun navigateToScreen() {
        // 미리보기에서 처리할 로직 (예: 아무것도 안 하거나 로그 출력)
    }
}

class DummyMainBaseViewModel(application: Application) : MainBaseViewModel(application) {
    // 필요한 상태나 메서드를 정의
}

// 미리보기를 위한 더미 MainScreenViewModel
class DummyMainScreenViewModel(application: Application) : MainScreenViewModel(application) {
    // 필요한 상태나 메서드를 정의
}