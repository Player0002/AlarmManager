package com.danny.alarmmanager.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.danny.alarmmanager.db.AlarmDao
import com.danny.alarmmanager.db.AlarmRepository
import com.danny.alarmmanager.models.AlarmItem
import kotlinx.coroutines.flow.collect

class MainViewModel(private val app: Application, private val dao: AlarmDao) :
    AndroidViewModel(app) {

    private val repository = AlarmRepository(app)

    val items = liveData {
        dao.getAlarms().collect { emit(it) }
    }

    fun deleteItem(alarmItem: AlarmItem) = repository.deleteAlarm(alarmItem)

    fun updateItem(alarmItem: AlarmItem) = repository.updateAlarm(alarmItem)
}