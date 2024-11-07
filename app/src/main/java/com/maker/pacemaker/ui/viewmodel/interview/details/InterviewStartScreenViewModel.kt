package com.maker.pacemaker.ui.viewmodel.interview.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.screen.interview.InterviewStartScreen
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class InterviewStartScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel
): ViewModel() {


}