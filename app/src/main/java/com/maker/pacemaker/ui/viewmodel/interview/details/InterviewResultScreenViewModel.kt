package com.maker.pacemaker.ui.viewmodel.interview.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.Interview
import com.maker.pacemaker.data.model.remote.sendCVResponse
import com.maker.pacemaker.ui.viewmodel.interview.InterviewBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class InterviewResultScreenViewModel @Inject constructor(
    private val base: InterviewBaseViewModel
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val interviewViewModel = base

    val repository = baseViewModel.repository

    private val _interviewResults = MutableStateFlow<List<sendCVResponse>>(emptyList())
    val interviewResults = _interviewResults

    private val _interviews = MutableStateFlow<Map<Int, List<Interview>>>(emptyMap()) // CV ID별 면접 질문, 답변, 점수 목록
    val interviews = _interviews

    fun onRefresh() {
        fetchMyCVs()

        if (interviewViewModel.interviewing.value) {
            viewModelScope.launch {
                // 10초 대기
                kotlinx.coroutines.delay(10000)
                // 면접 종료 상태로 변경
                interviewViewModel.setInterviewing(false)
            }
        }
    }

    private fun fetchMyCVs() {
        viewModelScope.launch {
            interviewViewModel.setLoading(true)

            try {
                val response = repository.getMyCVs() // CV 리스트 가져오기

                // CV 목록을 cv_id 기준으로 내림차순 정렬
                val sortedResponse = response.sortedByDescending { it.cv_id }

                // 정렬된 결과를 _interviewResults에 설정
                _interviewResults.value = sortedResponse

                // 각 CV에 대해 해당 면접 질문, 답변, 점수 정보 가져오기
                fetchInterviews(response.map { it.cv_id })
            } catch (e: Exception) {
                Log.e("InterviewResultScreenViewModel", "fetchMyCVs: $e")
                baseViewModel.triggerToast("응시한 면접 조회에 실패했습니다.")
            } finally {
                interviewViewModel.setLoading(false)
            }
        }
    }

    private fun fetchInterviews(cvIds: List<Int>) {
        viewModelScope.launch {
            try {
                val interviewMap = mutableMapOf<Int, List<Interview>>()

                for (cvId in cvIds) {
                    val interviewList = repository.getInterviewsByCVId(cvId) // CV ID를 기준으로 면접 질문 및 답변 정보 가져오기
                    interviewMap[cvId] = interviewList
                }

                _interviews.value = interviewMap
            } catch (e: Exception) {
                Log.e("InterviewResultScreenViewModel", "fetchInterviews: $e")
                baseViewModel.triggerToast("면접 질문을 가져오는 데 실패했습니다.")
            }
        }
    }
}