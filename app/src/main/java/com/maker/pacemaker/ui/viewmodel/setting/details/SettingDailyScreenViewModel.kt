package com.maker.pacemaker.ui.viewmodel.setting.details

import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class SettingDailyScreenViewModel @Inject constructor(): SettingBaseViewModel() {

    private val _myDailyCount = MutableStateFlow(0)
    val myDailyCount: MutableStateFlow<Int> get() = _myDailyCount

    fun setMyDailyCount(count: Int) {
        if (count >= 0) _myDailyCount.value = count
    }

    fun selectDailyCount(count: Int) {
        // 선택 정보 sharedpreference

        // repository

    }

}