package com.maker.pacemaker.di

import android.content.Context
import androidx.room.Room
import com.maker.pacemaker.data.model.db.AlarmDao
import com.maker.pacemaker.data.model.db.AlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    fun provideAlarmDatabase(context: Context): AlarmDatabase {
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideAlarmDao(database: AlarmDatabase): AlarmDao {
        return database.alarmDao()
    }
}
