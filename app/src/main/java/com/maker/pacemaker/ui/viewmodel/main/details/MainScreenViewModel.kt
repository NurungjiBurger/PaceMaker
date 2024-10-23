package com.maker.pacemaker.ui.viewmodel.main.details

import android.app.Application
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class MainScreenViewModel(application: Application) : MainBaseViewModel(application) {

    // MutableStateFlow로 balance 값을 관리
    private val _balance = MutableStateFlow(0L)
    val balance: StateFlow<Long> = _balance

    fun plusBalance() {
        _balance.value = _balance.value + 1
    }

}