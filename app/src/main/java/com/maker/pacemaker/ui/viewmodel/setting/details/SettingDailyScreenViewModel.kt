package com.maker.pacemaker.ui.viewmodel.setting.details

import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class SettingDailyScreenViewModel @Inject constructor(): SettingBaseViewModel() {

    private val _myDailyCount = MutableStateFlow(0)
    val myDailyCount: MutableStateFlow<Int> get() = _myDailyCount

    private val _dailySetting = MutableStateFlow(sharedPreferences.getString("dailySetting", "여유롭게"))
    val dailySetting: MutableStateFlow<String?> get() = _dailySetting

    init {
        _dailySetting.value = sharedPreferences.getString("dailySetting", "여유롭게")
    }

    fun selectDailySetting(setting: String, count: Int) {
        _dailySetting.value = setting
        if (count >= 0 && count <= 200) _myDailyCount.value = count
        else {

        }
    }

    fun completeDailySetting(result: Boolean) {
        if (result) {
            editor.putString("dailySetting", _dailySetting.value)
            editor.apply()
        }
        else {
            _dailySetting.value = sharedPreferences.getString("dailySetting", "여유롭게")
        }
    }

}