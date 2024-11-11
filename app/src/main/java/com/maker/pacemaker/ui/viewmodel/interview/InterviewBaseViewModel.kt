package com.maker.pacemaker.ui.viewmodel.interview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class InterviewBaseViewModel @Inject constructor(
    private val base: BaseViewModel
) : ViewModel() {

    val baseViewModel = base

    private val _text = MutableStateFlow("")
    val text = _text

    private val _interviewing = MutableStateFlow(false)
    val interviewing = _interviewing

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun setInterviewing(setInterviewing: Boolean) {
        Log.d("InterviewBaseViewModel", "setInterviewing: ${interviewing.value}")
        _interviewing.value = setInterviewing
        Log.d("InterviewBaseViewModel", "interviewing: ${interviewing.value}")
    }

    fun setLoading(setLoad: Boolean) {

        Log.d("InterviewBaseViewModel", "setLoading: ${isLoading}")

        Log.d("InterviewBaseViewModel", "setLoading: ${isLoading.value}")
        _isLoading.value = setLoad
        Log.d("InterviewBaseViewModel", "isLoading: ${isLoading.value}")
    }

    fun setText(text: String) {
        _text.value = text
    }

}