package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class MainProblemAddScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base

    private val _problem = MutableStateFlow("")
    val problem = _problem

    private val _anser = MutableStateFlow("")
    val answer = _anser

    private val _keyWord = MutableStateFlow("")
    val keyWord = _keyWord

    private val _keyWords = MutableStateFlow<List<String>>(emptyList())
    val keyWords = _keyWords

    fun onProblemChanged(problem: String) {
        _problem.value = problem
    }

    fun onAnswerChanged(answer: String) {
        _anser.value = answer
    }

    fun onKeyWordChanged(keyWord: String) {
        _keyWord.value = keyWord
    }

    fun onKeyWordEnroll() {
        if (_keyWords.value.size == 3) return

        val keyWord = _keyWord.value
        val keyWords = _keyWords.value.toMutableList()
        keyWords.add(keyWord)
        _keyWords.value = keyWords
        _keyWord.value = ""
    }

    fun onSubmit() {
        // 서버 제출

        baseViewModel.baseViewModel.triggerToast("문제가 추가가 신청되었습니다.")
        baseViewModel.baseViewModel.goScreen(ScreenType.MAIN)
    }

}