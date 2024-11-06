package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainProblemSearchScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base

    val repository = baseViewModel.repository

    private val _words = MutableStateFlow("")
    val words = _words

    private val _hashTags = MutableStateFlow<List<String>>(emptyList())
    val hashTags = _hashTags

    private val _searchedProblems = MutableStateFlow<List<Problem>>(emptyList())
    val searchedProblems = _searchedProblems

    init {
        restate()
    }

    fun restate() {
        _words.value = ""
        fetchProblems(_words.value)
    }

    private fun fetchProblems(searchKeyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProblemsByKeyWord(searchKeyword)

            if (response.isNotEmpty()) {
                _searchedProblems.value = response
            } else {
                _searchedProblems.value = emptyList()
            }
        }
    }

    fun onSearchButtonClicked() {
        // Repository를 통해 키워드로 문제를 검색합니다.
        val searchKeyword = _words.value
        fetchProblems(searchKeyword)
    }

    fun onSearchWordsChanged(words: String) {
        _words.value = words
    }
}