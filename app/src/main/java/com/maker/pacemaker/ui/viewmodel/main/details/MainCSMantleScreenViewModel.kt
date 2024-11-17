package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.ScreenType
import com.maker.pacemaker.data.model.remote.SimilarityWord
import com.maker.pacemaker.data.model.remote.SimilarityWordRequest
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
open class MainCSMantleScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base
    val repository = baseViewModel.repository

    private val _words = MutableStateFlow("")
    val words = _words

//    private val _correct = MutableStateFlow(false)
//    val correct = _correct

    private val _showModal = MutableStateFlow(false)
    val showModal = _showModal


    private val _submitedWords = MutableStateFlow<List<SimilarityWord>>(emptyList())
    val submitedWords = _submitedWords


    fun onSearchButtonClicked() {

        val searchKeyword = _words.value.trim()

        if (searchKeyword.isEmpty()) {
            baseViewModel.triggerToast("검색어가 비어 있습니다.", 1000) // Toast로 알림
            return
        }

        val isDuplicate = _submitedWords.value.any { it.word == searchKeyword }

        viewModelScope.launch {
            val request = SimilarityWordRequest(word = searchKeyword)

            try {
                val response = repository.checkWordSimilarity(request)

                if (response.similarity == 100.0f){
                    baseViewModel.setCSMantleSolved(true)
                    _showModal.value = true
                }
                if (!isDuplicate) {
                    val similarity = response.similarity
                    val rank = response.ranking

                    val newWords = _submitedWords.value + SimilarityWord(searchKeyword, similarity, rank)
                    _submitedWords.value = newWords.sortedByDescending { it.similarity }

                    _words.value = ""
                } else {
                    baseViewModel.triggerToast("중복된 단어입니다.") // 중복 알림
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.e("MainCSMantleScreenViewModel", "Error: 단어를 찾을 수 없습니다.")
                    baseViewModel.triggerToast("단어를 찾을 수 없습니다.", 500)
                } else {
                    Log.e("MainCSMantleScreenViewModel", "Error checking word similarity", e)
                    baseViewModel.triggerToast("단어 유사도를 확인하는 중 오류가 발생했습니다.", 500)
                }
            } catch (e: Exception) {
                Log.e("MainCSMantleScreenViewModel", "Unexpected error checking word similarity", e)
                baseViewModel.triggerToast("예기치 않은 오류가 발생했습니다.", 500)
            }
        }
    }

    fun onSearchWordsChanged(words: String) {
        _words.value = words
    }

    fun hideModal() {
        _showModal.value = false
        baseViewModel.goScreen(ScreenType.CSRANKING)
    }



}
