package com.maker.pacemaker.ui.viewmodel.interview.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class InterviewResultScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel
): ViewModel() {

}