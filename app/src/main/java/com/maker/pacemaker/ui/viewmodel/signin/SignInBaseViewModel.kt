package com.maker.pacemaker.ui.viewmodel.signin

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SignInBaseViewModel @Inject constructor(
    private val base: BaseViewModel
) : ViewModel() {

    val baseViewModel = base
}