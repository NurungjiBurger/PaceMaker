package com.maker.pacemaker.ui.viewmodel.setting.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class SettingRatioScreenViewModel @Inject constructor(
    private val base: SettingBaseViewModel
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val settingViewModel = base

    fun selectRatioSetting(setting: String) {
        settingViewModel._ratioMode.value = setting
    }

    fun completeRatioSetting(result: Boolean) {
        if (result) {
            baseViewModel.editor.putString("ratioMode", settingViewModel._ratioMode.value)
            baseViewModel.editor.apply()

            settingViewModel._ratioMode.value =
                baseViewModel.sharedPreferences.getString("ratioMode", "일반 모드").toString()
            Log.d("SettingRatioScreenViewModel", "${settingViewModel._ratioMode.value} Ratio Setting Saved")
            Log.d("SettingRatioScreenViewModel", "Ratio Setting Saved")
        }
        else {
            settingViewModel._ratioMode.value =
                baseViewModel.sharedPreferences.getString("ratioMode", "일반 모드").toString()
        }
    }

}