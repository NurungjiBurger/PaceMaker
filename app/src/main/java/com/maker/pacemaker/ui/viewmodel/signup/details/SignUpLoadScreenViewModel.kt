package com.maker.pacemaker.ui.viewmodel.signup.details

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.maker.pacemaker.ui.viewmodel.signup.SignUpBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
open class SignUpLoadScreenViewModel @Inject constructor(
    val base: SignUpBaseViewModel
) : ViewModel() {

    val baseViewModel = base
}