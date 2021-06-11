package com.danny.alarmmanager.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.danny.alarmmanager.db.AlarmDao

class AddAlarmViewModelFactory(private val app: Application, private val dao: AlarmDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddAlarmViewModel(app, dao) as T
    }
}