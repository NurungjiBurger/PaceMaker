package com.maker.pacemaker.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alarmType: String,
    val content: String,
    val dateTime: Long,
)