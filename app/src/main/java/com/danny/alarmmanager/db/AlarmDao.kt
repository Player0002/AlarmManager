package com.danny.alarmmanager.db

import androidx.room.*
import com.danny.alarmmanager.models.AlarmItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("select * from alarm_table")
    fun getAlarms(): Flow<List<AlarmItem>>

    @Query("select * from alarm_table where id = :id")
    fun getAlarm(id: Long): AlarmItem?

    @Insert
    suspend fun addAlarm(item: AlarmItem): Long

    @Delete
    suspend fun deleteAlarm(item: AlarmItem)

    @Update
    suspend fun updateAlarm(item: AlarmItem)

}