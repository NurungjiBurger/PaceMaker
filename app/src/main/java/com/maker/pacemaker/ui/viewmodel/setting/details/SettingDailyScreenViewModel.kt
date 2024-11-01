package com.maker.pacemaker.ui.viewmodel.setting.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.remote.DailyCntRequest
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SettingDailyScreenViewModel @Inject constructor(
    private val base: SettingBaseViewModel
): ViewModel() {

    val baseViewModel = base

    val repository = baseViewModel.baseViewModel.repository

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
            baseViewModel.baseViewModel.editor.putInt("myDailyCount", baseViewModel._dailyCount.value)
            baseViewModel.baseViewModel.editor.apply()

            Log.d("SettingDailyScreenViewModel", "dailySetting: ${_dailySetting.value}, dailyCount: ${baseViewModel._dailyCount.value}")

            CoroutineScope(Dispatchers.IO).launch {
                val request = DailyCntRequest(baseViewModel._dailyCount.value)

                repository.updateDailyCnt(request)
                Log.d("SettingDailyScreenViewModel", "dailyCount updated")
            }
        }
        else {
            _dailySetting.value = baseViewModel.baseViewModel.sharedPreferences.getString("dailySetting", "여유롭게")
        }
    }

}