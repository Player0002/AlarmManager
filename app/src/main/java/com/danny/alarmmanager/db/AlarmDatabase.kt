package com.danny.alarmmanager.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danny.alarmmanager.models.AlarmItem

@Database(entities = [AlarmItem::class], version = 1, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun getDao(): AlarmDao

    companion object {
        @Volatile
        var instance: AlarmDatabase? = null

        fun getInstance(application: Application): AlarmDatabase {
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(application, AlarmDatabase::class.java, "alarm_db")
                    .fallbackToDestructiveMigration().build()
                instance!!
            }
        }
    }
}