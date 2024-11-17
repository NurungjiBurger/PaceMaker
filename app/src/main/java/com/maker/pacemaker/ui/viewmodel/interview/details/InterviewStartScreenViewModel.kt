package com.maker.pacemaker.ui.viewmodel.interview.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class InterviewStartScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val interviewViewModel = base

    fun onTextChanged(text: String) {
        if (text.length > 3000) {
            baseViewModel.triggerToast("더이상 입력하실 수 없습니다.")
            baseViewModel.triggerVibration()
            return
        }
        interviewViewModel.setText(text)
    }

    fun setting(interview: Boolean, loading: Boolean) {
        interviewViewModel.setInterviewing(interview)
        interviewViewModel.setLoading(loading)
    }

    fun onResult() {
        viewModelScope.launch {
            setting(false, true)

            baseViewModel.goScreen(ScreenType.INTERVIEWRESULT)
        }
    }

    fun onSubmit() {
        viewModelScope.launch {
            setting(true, true)

            baseViewModel.goScreen(ScreenType.INTERVIEWING)
        }
    }


    fun canNotSubmit() {
        baseViewModel.triggerToast("최소 ${350 - interviewViewModel.text.value.length}자 더 입력해주세요.")
        baseViewModel.triggerVibration()
    }

}