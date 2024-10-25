package com.maker.pacemaker.ui.viewmodel.setting.details

import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.setting.SettingBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class SettingCategoryScreenViewModel @Inject constructor(
    private val base: SettingBaseViewModel
): ViewModel() {

    val baseViewModel = base

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: MutableStateFlow<String> get() = _selectedCategory

    init {
        _selectedCategory.value = baseViewModel.baseViewModel.sharedPreferences.getString("selectedCategory", "01234") ?: "01234"
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }
}