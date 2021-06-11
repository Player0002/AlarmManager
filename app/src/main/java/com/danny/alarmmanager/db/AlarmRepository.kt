package com.danny.alarmmanager.db

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.danny.alarmmanager.models.AlarmItem
import com.danny.alarmmanager.receiver.AlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AlarmRepository(private val context: Context) {
    private val application = context.applicationContext as Application
    private val dao = AlarmDatabase.getInstance(application).getDao()
    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    private val notifyIntent = Intent(application, AlarmReceiver::class.java)

    suspend fun getAlarm(id: Long) = dao.getAlarm(id)

    fun deleteAlarm(alarmItem: AlarmItem) = CoroutineScope(Dispatchers.IO).launch {
        if (alarmItem.enabled) {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    application,
                    alarmItem.id.toInt(),
                    notifyIntent,
                    PendingIntent.FLAG_NO_CREATE
                )
            )
        }
        dao.deleteAlarm(alarmItem)
    }

    fun setAlarm(alarmItem: AlarmItem) = CoroutineScope(Dispatchers.IO).launch {
        val response =
            dao.addAlarm(alarmItem)

        val pending = PendingIntent.getBroadcast(
            application,
            response.toInt(),
            notifyIntent.apply { putExtra("code", response) },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmItem.time,
            pending
        )
    }

    fun updateAlarm(alarmItem: AlarmItem) = CoroutineScope(Dispatchers.IO).launch {
        if (alarmItem.time < Calendar.getInstance().timeInMillis) alarmItem.enabled = false
        val pending = PendingIntent.getBroadcast(
            application,
            alarmItem.id.toInt(),
            notifyIntent.apply {
                putExtra("code", alarmItem.id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (!alarmItem.enabled) {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    application,
                    alarmItem.id.toInt(),
                    notifyIntent,
                    PendingIntent.FLAG_NO_CREATE
                )
            )
        } else {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                alarmItem.time,
                pending
            )
        }
        dao.updateAlarm(alarmItem)
    }
}