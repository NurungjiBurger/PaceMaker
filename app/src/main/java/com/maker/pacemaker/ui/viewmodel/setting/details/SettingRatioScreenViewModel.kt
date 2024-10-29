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

    val baseViewModel = base

    fun selectRatioSetting(setting: String) {
        baseViewModel._ratioMode.value = setting
    }

    fun completeRatioSetting(result: Boolean) {
        if (result) {
            baseViewModel.baseViewModel.editor.putString("ratioMode", baseViewModel._ratioMode.value)
            baseViewModel.baseViewModel.editor.apply()

            baseViewModel._ratioMode.value =
                baseViewModel.baseViewModel.sharedPreferences.getString("ratioMode", "일반 모드").toString()
            Log.d("SettingRatioScreenViewModel", "${baseViewModel._ratioMode.value} Ratio Setting Saved")
            Log.d("SettingRatioScreenViewModel", "Ratio Setting Saved")
        }
        else {
            baseViewModel._ratioMode.value =
                baseViewModel.baseViewModel.sharedPreferences.getString("ratioMode", "일반 모드").toString()
        }
    }

}