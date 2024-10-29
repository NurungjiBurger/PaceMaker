package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.Problem
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainProblemSearchScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base

    val repository = baseViewModel.baseViewModel.repository

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
        _hashTags.value = emptyList()
        _searchedProblems.value = emptyList()
    }

    fun onSearchButtonClicked() {

        // Repository를 통해 키워드로 문제를 검색합니다.
        val searchKeyword = _words.value

        // repository.getProblemsByKeyWord 메서드를 사용하여 검색 결과를 가져옵니다.
        viewModelScope.launch {
            val result = repository.getProblemsByKeyWord(searchKeyword)

            // 검색 결과가 비어 있지 않으면 _searchedProblems와 _hashTags를 업데이트합니다.
            if (result.isNotEmpty()) {
                _searchedProblems.value = result

                // 해시태그를 생성하는 로직 (예: 문제 설명에서 해시태그 추출)
//                val extractedHashTags = result.flatMap { it.second.split(" ") } // 예시: 문제 설명에서 단어를 추출하여 해시태그로 사용
//                    .distinct() // 중복 제거
//                    .filter { it.isNotEmpty() } // 빈 문자열 제거

   //             _hashTags.value = extractedHashTags
            } else {
                // 검색 결과가 없을 경우
                _searchedProblems.value = emptyList()
                _hashTags.value = emptyList()
            }
        }
    }

    fun onSearchWordsChanged(words: String) {
        _words.value = words
    }
}