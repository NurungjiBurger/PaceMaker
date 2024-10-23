package com.maker.pacemaker.ui.viewmodel.main.details

import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmDatabase
import com.maker.pacemaker.data.model.db.AlarmEntity
import com.maker.pacemaker.data.model.repository.AlarmRepository
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class MainAlarmScreenViewModel @Inject constructor(
    private val alarmDao: AlarmDao
) : MainBaseViewModel() {

    private val alarmRepository = AlarmRepository(alarmDao)

    private val _alarms = MutableStateFlow<List<AlarmEntity>>(emptyList())
    val alarms: StateFlow<List<AlarmEntity>> get() = _alarms

    init {
        // Repository 초기화
        loadAlarms()
    }

    private fun loadAlarms() {
        viewModelScope.launch {
            alarmRepository.getAllAlarms().collect { alarmList ->
                _alarms.value = alarmList.sortedByDescending { it.id } // ID 기준 내림차순 정렬
            }
        }
    }

    fun deleteAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            alarmRepository.deleteAlarm(alarm)
            loadAlarms()
        }
    }

}