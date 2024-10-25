package com.maker.pacemaker.data.model.test

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainAlarmScreenViewModel
import com.maker.pacemaker.ui.viewmodel.main.details.MainScreenViewModel
import com.maker.pacemaker.ui.viewmodel.sign.SignBaseViewModel
import com.maker.pacemaker.ui.viewmodel.sign.details.SignUpScreenViewModel

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
class DummySignBaseViewModel() : SignBaseViewModel() {
    // 필요한 상태나 메서드를 정의
}

class DummySignUpScreenViewModel : SignUpScreenViewModel(FirebaseAuth.getInstance()) {
    // 필요한 상태나 메서드를 추가로 정의
}