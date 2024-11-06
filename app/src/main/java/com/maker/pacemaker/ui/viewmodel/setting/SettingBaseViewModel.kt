package com.maker.pacemaker.ui.viewmodel.setting

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class SettingBaseViewModel @Inject constructor(
    private val base: BaseViewModel
) : ViewModel() {

    val baseViewModel = base

    val _ratioMode = MutableStateFlow("일반 모드")
    val ratioMode: MutableStateFlow<String> get() = _ratioMode

    init {
        baseViewModel._dailyCount.value = baseViewModel.sharedPreferences.getInt("myDailyCount", 30)
        _ratioMode.value = baseViewModel.sharedPreferences.getString("ratioMode", "일반 모드") ?: "일반 모드"

        // fetch category list
    }

    fun setRatioMode(mode: String) {
        _ratioMode.value = mode
    }
}