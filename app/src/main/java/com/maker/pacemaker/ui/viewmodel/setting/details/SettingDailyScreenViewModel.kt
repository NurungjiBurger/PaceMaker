package com.maker.pacemaker.ui.viewmodel.setting.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class SettingDailyScreenViewModel @Inject constructor(
    private val base: SettingBaseViewModel
): ViewModel() {

    val baseViewModel = base

    private val _dailySetting = MutableStateFlow(baseViewModel.baseViewModel.sharedPreferences.getString("dailySetting", "여유롭게"))
    val dailySetting: MutableStateFlow<String?> get() = _dailySetting

    init {
        _dailySetting.value = baseViewModel.baseViewModel.sharedPreferences.getString("dailySetting", "여유롭게")
    }

    fun selectDailySetting(setting: String, count: Int) {
        _dailySetting.value = setting
        if (count >= 0 && count <= 200) baseViewModel._dailyCount.value = count
        else {

        }
    }

    fun completeDailySetting(result: Boolean) {
        if (result) {
            baseViewModel.baseViewModel.editor.putString("dailySetting", _dailySetting.value)
            baseViewModel.baseViewModel.editor.apply()
        }
        else {
            _dailySetting.value = baseViewModel.baseViewModel.sharedPreferences.getString("dailySetting", "여유롭게")
        }
    }

}