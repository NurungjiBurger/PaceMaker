package com.maker.pacemaker.ui.viewmodel.main.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmEntity
import com.maker.pacemaker.data.model.repository.AlarmRepository
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainAlarmScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
    alarmDao: AlarmDao
) : ViewModel() {

    val baseViewModel = base.baseViewModel
    val mainViewModel = base

    private val alarmRepository = AlarmRepository(alarmDao)

    private val _alarms = MutableStateFlow<List<AlarmEntity>>(emptyList())
    val alarms: StateFlow<List<AlarmEntity>> get() = _alarms

    init {
        loadAlarms() // 초기 알람 로드
    }

    fun reloadAlarms() {
        loadAlarms()
    }

    private fun loadAlarms() {
        _alarms.value = emptyList() // 초기화
        viewModelScope.launch {
            alarmRepository.getAllAlarms().collect { alarmList ->
                _alarms.value = alarmList // 정렬 없이 그대로 할당
                Log.d("MainAlarmScreenViewModel", "Alarms loaded: $alarmList") // 로드된 알람 출력
            }
        }
    }

    fun deleteAlarm(alarmId: Long) {
        viewModelScope.launch {
            alarmRepository.deleteAlarmById(alarmId) // ID로 알람 삭제
        }
        loadAlarms()
    }

    fun deleteAllAlarms() {
        viewModelScope.launch {
            alarmRepository.deleteAllAlarms() // 모든 알람 삭제
        }
        loadAlarms()
    }
}