package com.maker.pacemaker.ui.viewmodel.setting.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SettingMyPageScreenViewModel @Inject constructor(
    val base: SettingBaseViewModel
) : ViewModel() {

    val baseViewModel = base.baseViewModel

    fun dataRefresh(){
        baseViewModel.getUserInfo()
    }

}