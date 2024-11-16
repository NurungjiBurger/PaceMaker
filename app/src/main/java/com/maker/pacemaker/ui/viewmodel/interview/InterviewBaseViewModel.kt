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

    // 자기소개서 번호
    private val _cvId = MutableStateFlow(-1)
    val cvId = _cvId

    private val _text = MutableStateFlow("")
    val text = _text

    private val _interviewing = MutableStateFlow(false)
    val interviewing = _interviewing

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    fun setCVId(cvId: Int) {
        _cvId.value = cvId
    }

    fun setInterviewing(setInterviewing: Boolean) {
        _interviewing.value = setInterviewing
    }

    fun setLoading(setLoad: Boolean) {
        _isLoading.value = setLoad
    }

    fun setText(text: String) {
        _text.value = text
    }

}