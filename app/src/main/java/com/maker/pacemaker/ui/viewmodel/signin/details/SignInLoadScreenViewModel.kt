package com.maker.pacemaker.ui.viewmodel.signin.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.signin.SignInBaseViewModel
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SignInLoadScreenViewModel @Inject constructor(
    private val base: SignUpBaseViewModel
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val signInViewModel = base
}