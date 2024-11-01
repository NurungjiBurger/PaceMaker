package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.SimilarityWord
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainCSMantleScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base

    private val _words = MutableStateFlow("")
    val words = _words

    private val _submitedWords = MutableStateFlow<List<SimilarityWord>>(emptyList())
    val submitedWords = _submitedWords

    init {
        _submitedWords.value = listOf(
            SimilarityWord("사과", 0.9, 1),
            SimilarityWord("바나나", 0.8, 2),
            SimilarityWord("포도", 0.7, 3),
            SimilarityWord("딸기", 0.6, 4),
            SimilarityWord("수박", 0.5, 5),
            SimilarityWord("참외", 0.4, 6),
            SimilarityWord("키위", 0.3, 7),
            SimilarityWord("망고", 0.2, 8),
            SimilarityWord("복숭아", 0.1, 9),
            SimilarityWord("자두", 0.0, 10),
        )
    }

    fun onSearchButtonClicked() {
        
        val searchKeyword = _words.value.trim()
        if (searchKeyword.isEmpty()) {
            println("검색어가 비어 있습니다.") // 필요에 따라 메시지를 출력하거나 별도 처리
            return
        }

        // 이미 존재하는 단어인지 확인
        val isDuplicate = _submitedWords.value.any { it.word == searchKeyword }


        if (!isDuplicate) {
            // 유사도를 0.0에서 1.0 사이로 임의로 설정
            val similarity = (0..100).random() / 100.0
            val rank = _submitedWords.value.size + 1 // 새로운 단어의 순위를 임의로 설정

            // 새로운 단어를 추가하고 유사도 순으로 내림차순 정렬하여 업데이트
            val newWords = _submitedWords.value + SimilarityWord(searchKeyword, similarity, rank)
            _submitedWords.value = newWords.sortedBy { it.similarity }

            _words.value = "" // 입력 초기화
        } else {
            // 중복일 경우, 알림을 추가하거나 별도의 처리를 할 수 있습니다.
            println("중복된 단어입니다.")
        }
    }


    fun onSearchWordsChanged(words: String) {
        _words.value = words
    }
}