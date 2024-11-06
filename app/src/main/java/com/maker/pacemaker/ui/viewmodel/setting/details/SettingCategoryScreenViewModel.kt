package com.maker.pacemaker.ui.viewmodel.setting.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _settingCategories = MutableStateFlow<List<String>>(listOf())
    val settingCategories = _settingCategories.asStateFlow()

    // 선택된 카테고리를 관리하는 리스트
    private val _selectedCategories = MutableStateFlow<String>("")
    val selectedCategories = _selectedCategories.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // API로부터 카테고리 정보를 가져옴
            val response = repository.getCategories() // 예: getCategories()는 API 호출 함수
            val categoryCount = response.categories.size

            // `preferred_categories` 초기값 설정
            val savedCategories = baseViewModel.sharedPreferences.getString("preferred_categories", null)

            val initialSelection = if (savedCategories != null) {
                if (savedCategories.length == categoryCount) {
                    // 저장된 값이 있고, 카테고리 수와 길이가 같으면 그대로 사용
                    savedCategories
                } else {
                    // 저장된 값이 있지만, 카테고리 수와 길이가 다르면 부족한 부분을 `0`으로 채워 길이를 맞춤
                    savedCategories.padEnd(categoryCount, '0')
                }
            } else {
                // 저장된 값이 없는 경우 모든 카테고리를 선택한 기본값("111111...") 설정
                "1".repeat(categoryCount)
            }

            // _selectedCategories를 초기화
            _selectedCategories.value = initialSelection

            // SharedPreferences에 저장
//            baseViewModel.sharedPreferences.edit()
//                .putString("preferred_categories", initialSelection)
//                .apply()
        }
    }

    // 카테고리 선택/해제
    fun toggleCategorySelection(categoryIndex: Int) {
        viewModelScope.launch {
            // 현재 _selectedCategories 값을 가져와서 특정 인덱스의 값을 변경
            val currentSelection = _selectedCategories.value
            if (categoryIndex in currentSelection.indices) {
                // 선택 여부를 반전 (0 -> 1, 1 -> 0)
                val updatedSelection = currentSelection.mapIndexed { index, char ->
                    if (index == categoryIndex) {
                        if (char == '0') '1' else '0' // 0은 선택되지 않음, 1은 선택됨
                    } else {
                        char
                    }
                }.joinToString("")

                // 업데이트된 선택 상태를 _selectedCategories에 반영
                _selectedCategories.value = updatedSelection
            }
        }
    }

    // 선택 완료
    fun completeSelection() {
        viewModelScope.launch {
            // 변경된 _selectedCategories 값을 SharedPreferences에 저장
            val selectedCategoriesString = _selectedCategories.value
            baseViewModel.sharedPreferences.edit()
                .putString("preferred_categories", selectedCategoriesString)
                .apply()

            // API 호출 로직을 여기에 추가 (예: API 요청을 보내기 위해 repository 호출)
            // repository.updateCategories(selectedCategoriesString)
        }
    }
}