package com.maker.pacemaker.data.model.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveAlarmWorker(
    @ApplicationContext appContext: Context,
    workerParams: WorkerParameters,
    private val alarmDao: AlarmDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val title = inputData.getString("title") ?: return@withContext Result.failure()
        val message = inputData.getString("message") ?: return@withContext Result.failure()

        try {
            // Room DB에 알람 저장
            val newAlarm = AlarmEntity(alarmType = title, content = message, dateTime = System.currentTimeMillis())
            alarmDao.insertAlarm(newAlarm)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
