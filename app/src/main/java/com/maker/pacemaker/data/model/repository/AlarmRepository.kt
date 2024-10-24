package com.maker.pacemaker.data.model.repository

import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmEntity
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {
    suspend fun insertAlarm(alarm: AlarmEntity) {
        alarmDao.insertAlarm(alarm)
    }

    fun getAllAlarms(): Flow<List<AlarmEntity>> {
        return alarmDao.getAllAlarms()
    }

    suspend fun deleteAlarmById(alarmId: Long) {
        alarmDao.deleteAlarmById(alarmId) // ID로 알람 삭제
    }
}