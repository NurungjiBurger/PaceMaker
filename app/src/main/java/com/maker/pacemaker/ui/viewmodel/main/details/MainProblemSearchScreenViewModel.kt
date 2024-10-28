package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class MainProblemSearchScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base

    private val _words = MutableStateFlow("")
    val words = _words

    private val _hashTags = MutableStateFlow<List<String>>(emptyList())
    val hashTags = _hashTags

    private val _searchedProblems = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val searchedProblems = _searchedProblems

    init {
        restate()
    }

    fun restate() {
        _words.value = ""
        _hashTags.value = emptyList()
        _searchedProblems.value = emptyList()
    }

    fun onSearchButtonClicked() {
        if (_words.value == "SRP") {
            _searchedProblems.value = listOf(
                Pair("단일 책임 원칙(SRP)", "객체 지향의 SOLID 중 \"클래스는 단 하나의 목적을 가져야 하며..."),
                Pair("개방 폐쇄 원칙(OCP)", "객체 지향의 SOLID 중 \"소프트웨어 요소는 확장에는 열려 있으나 변경..."),
            )
            hashTags.value = listOf("OOP", "SOLID")
        }
        else {
            _searchedProblems.value = emptyList()
            hashTags.value = emptyList()
        }
    }

    fun onSearchWordsChanged(words: String) {
        _words.value = words
    }
}