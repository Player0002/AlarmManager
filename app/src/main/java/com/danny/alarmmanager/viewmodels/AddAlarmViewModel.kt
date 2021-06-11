package com.danny.alarmmanager.viewmodels

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.danny.alarmmanager.db.AlarmDao
import com.danny.alarmmanager.db.AlarmRepository
import com.danny.alarmmanager.models.AlarmItem
import kotlinx.coroutines.launch
import java.util.*

class AddAlarmViewModel(private val app: Application, private val dao: AlarmDao) :
    AndroidViewModel(app) {

    private val repository = AlarmRepository(app)

    private val calendar = Calendar.getInstance().clone() as Calendar
    private val _datetime = MutableLiveData<Long>()
    val datetime: LiveData<String>
        get() = _datetime.map {
            calendar.timeInMillis = it
            "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH) + 1}/${
                calendar.get(
                    Calendar.DAY_OF_MONTH
                )
            }"
        }
    private val _color = MutableLiveData<String>()
    val color: LiveData<String> get() = _color

    init {
        _datetime.value = calendar.timeInMillis
        _color.value = "#FF000000"
    }

    fun updateTime(y: Int, m: Int, d: Int) {
        calendar.set(y, m, d)
        _datetime.value = calendar.timeInMillis
    }

    fun updateHour(h: Int, mm: Int) {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH)
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(y, m, d, h, mm, 0)
        _datetime.value = calendar.timeInMillis
    }

    fun updateColor(hex: String) {
        _color.value = hex
    }

    fun saveAlarm(title: String, contents: String) = viewModelScope.launch {
        val time = _datetime.value!!
        Log.d("TAG", "Alarm set : $time ${SystemClock.elapsedRealtime()}")
        val item = AlarmItem(0, title, contents, time, true, color.value!!)
        repository.setAlarm(item)
    }
}