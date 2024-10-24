package com.maker.pacemaker.data.model.test

import android.app.Application
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMenuScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainMyPageScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel

class PreviewTest {
}

// 모의 Application 클래스
class MockApplication : Application()

// 미리보기를 위한 더미 ViewModel
class DummyBaseViewModel() : BaseViewModel() {
    fun navigateToScreen() {
        // 미리보기에서 처리할 로직 (예: 아무것도 안 하거나 로그 출력)
    }
}

class DummyMainBaseViewModel() : MainBaseViewModel() {
    // 필요한 상태나 메서드를 정의
}

// 미리보기를 위한 더미 MainScreenViewModel
class DummyMainScreenViewModel() : MainScreenViewModel() {
    // 필요한 상태나 메서드를 정의
}

class DummyMainAlarmScreenViewModel(alarmDao: AlarmDao) : MainAlarmScreenViewModel(alarmDao) {
    // 필요한 상태나 메서드를 정의
}

class DummyMainMenuScreenViewModel() : MainMenuScreenViewModel() {
    // 필요한 상태나 메서드를 정의
}

class DummyMainMyPageScreenViewModel() : MainMyPageScreenViewModel() {
    // 필요한 상태나 메서드를 정의
}