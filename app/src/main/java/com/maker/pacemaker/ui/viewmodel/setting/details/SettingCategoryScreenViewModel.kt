package com.maker.pacemaker.ui.viewmodel.setting.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.remote.Category
import com.maker.pacemaker.data.model.remote.updateCategoriesRequest
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingCategoryScreenViewModel @Inject constructor(
    private val base: SettingBaseViewModel
): ViewModel() {

    val baseViewModel = base.baseViewModel
    val settingViewModel = base

    val repository = baseViewModel.repository

    // 카테고리 목록 (Int 타입)
    private val _settingCategories = MutableStateFlow<List<Category>>(listOf())
    val settingCategories = _settingCategories.asStateFlow()

    // 선택된 카테고리 (Set<Int>)
    private val _selectedCategories = MutableStateFlow<Set<Int>>(setOf())
    val selectedCategories = _selectedCategories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // API로부터 카테고리 정보를 가져옴
            val response = repository.getCategories() // 예: getCategories()는 API 호출 함수
            val categoryCount = response.categories.size

            // category_id를 기준으로 오름차순으로 정렬
            val sortedCategories = response.categories.sortedBy { it.category_id }

            // 정렬된 카테고리 정보를 _settingCategories에 저장
            _settingCategories.value = sortedCategories

            Log.d("SettingCategoryScreenViewModel", "response: $response")

            // `preferred_categories` 초기값 설정
            val savedCategories = baseViewModel.sharedPreferences.getStringSet("preferred_categories", null)

            val initialSelection = if (savedCategories != null && savedCategories.isNotEmpty()) {
                // 저장된 카테고리 아이디들을 Set<Int> 형태로 사용
                savedCategories.map { it.toInt() }.toSet()
            } else {
                // 기본값으로 모든 카테고리 선택 (여기서는 예시로 1번부터 카테고리 개수까지)
                (0..categoryCount - 1).toSet()
            }

            // _selectedCategories를 초기화
            _selectedCategories.value = initialSelection
        }
    }

    // 카테고리 선택/해제
    fun toggleCategorySelection(categoryId: Int) {
        viewModelScope.launch {
            // 현재 _selectedCategories 값을 가져와서 선택/해제 처리
            val currentSelection = _selectedCategories.value
            val updatedSelection = if (currentSelection.contains(categoryId)) {
                // 카테고리 선택 해제
                currentSelection - categoryId
            } else {
                // 카테고리 선택
                currentSelection + categoryId
            }

            // 업데이트된 선택 상태를 _selectedCategories에 반영
            _selectedCategories.value = updatedSelection

            // 업데이트된 선택 상태를 로그에 출력
            Log.d("SettingCategoryScreenViewModel", "updatedSelection: $updatedSelection")
        }
    }

    // 선택 완료
    fun completeSelection() {
        viewModelScope.launch {
            // 변경된 _selectedCategories 값을 SharedPreferences에 저장
            val selectedCategoriesSet = _selectedCategories.value
            baseViewModel.sharedPreferences.edit()
                .putStringSet("preferred_categories", selectedCategoriesSet.map { it.toString() }.toSet())  // Set<Int>를 String으로 변환하여 저장
                .apply()

            // API 호출 로직을 여기에 추가 (예: API 요청을 보내기 위해 repository 호출)
            // repository.updateCategories(selectedCategoriesSet)
            val request = updateCategoriesRequest(selectedCategoriesSet.toList()) // 예: updateCategoriesRequest는 API 요청 클래스
            repository.updateCategories(request) // 예: updateCategories()는 API 호출 함수
        }
    }
}