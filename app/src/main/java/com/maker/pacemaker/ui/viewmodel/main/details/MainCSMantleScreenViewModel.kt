package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.SimilarityWord
import com.maker.pacemaker.data.model.remote.SimilarityWordRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainCSMantleScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base
    val repository = baseViewModel.repository

    private val _words = MutableStateFlow("")
    val words = _words

    private val _submitedWords = MutableStateFlow<List<SimilarityWord>>(emptyList())
    val submitedWords = _submitedWords

    init {

    }

    fun onSearchButtonClicked() {
        val searchKeyword = _words.value.trim()

        if (searchKeyword.isEmpty()) {
            println("검색어가 비어 있습니다.") // 필요에 따라 메시지를 출력하거나 별도 처리
            return
        }

        // 이미 존재하는 단어인지 확인
        val isDuplicate = _submitedWords.value.any { it.word == searchKeyword }

        // 비동기 작업을 위해 코루틴을 시작합니다
        viewModelScope.launch {
            val request = SimilarityWordRequest(word = searchKeyword)

            try {
                val response = repository.checkWordSimilarity(request)

                // Log.d를 사용하여 response 전체를 출력
                Log.d("MainCSMantleScreenViewModel", "Response: $response")
                Log.d("MainCSMantleScreenViewModel", "Try_cnt: ${response.try_cnt}, Similarity: ${response.similarity}, Rank: ${response.ranking}")

                if (!isDuplicate) {
                    // 유사도와 순위를 API 응답으로부터 설정
                    val similarity = response.similarity
                    val rank = response.ranking

                    // 새로운 단어를 추가하고 유사도 순으로 내림차순 정렬하여 업데이트
                     val newWords = _submitedWords.value + SimilarityWord(searchKeyword, similarity, rank)
                    _submitedWords.value = newWords.sortedByDescending { it.similarity }

                    _words.value = "" // 입력 초기화
                } else {
                    // 중복일 경우, 알림을 추가하거나 별도의 처리를 할 수 있습니다.
                    println("중복된 단어입니다.")
                }
            } catch (e: Exception) {
                Log.e("MainCSMantleScreenViewModel", "Error checking word similarity", e)
            }
        }
    }



    fun onSearchWordsChanged(words: String) {
        _words.value = words
    }
}