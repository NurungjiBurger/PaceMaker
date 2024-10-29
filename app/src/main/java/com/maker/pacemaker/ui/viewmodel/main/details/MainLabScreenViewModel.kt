package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class MainLabScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base

}