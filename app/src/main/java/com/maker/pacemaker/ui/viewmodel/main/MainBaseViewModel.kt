package com.maker.pacemaker.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class MainBaseViewModel @Inject constructor(
    private val base: BaseViewModel
) : ViewModel() {

    val baseViewModel = base

    // `allSolved` 상태를 관리하는 StateFlow 선언
    private val _allSolved = MutableStateFlow(false)
    val allSolved: StateFlow<Boolean> get() = _allSolved


    // `allSolved` 값을 변경할 함수
    fun setAllSolved(value: Boolean) {
        _allSolved.value = value
    }
}
