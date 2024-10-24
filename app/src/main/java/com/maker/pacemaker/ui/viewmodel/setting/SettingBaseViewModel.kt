package com.maker.pacemaker.ui.viewmodel.setting

import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class SettingBaseViewModel : BaseViewModel() {

    private val _dailyCount = MutableStateFlow(0)
    val dailyCount get() = _dailyCount

    private val _ratioMode = MutableStateFlow("일반 모드")
    val ratioMode get() = _ratioMode

    private val _categoryList = MutableStateFlow(emptyList<String>())
    val categoryList get() = _categoryList

    fun setDailyCount(count: Int) {
        _dailyCount.value = count
    }

    fun setRatioMode(mode: String) {
        _ratioMode.value = mode
    }

    fun setCategoryList(list: List<String>) {
        _categoryList.value = list
    }
}