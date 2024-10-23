package com.maker.pacemaker.data.model.repository

import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmEntity
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {

    suspend fun insertAlarm(alarm: AlarmEntity) {
        alarmDao.insertAlarm(alarm)
    }

    fun getAllAlarms(): Flow<List<AlarmEntity>> {
        return alarmDao.getAllAlarms() // Flow<List<AlarmEntity>>
    }

    suspend fun deleteAlarm(alarm: AlarmEntity) {
        alarmDao.deleteAlarm(alarm) // 알람 삭제
    }
}