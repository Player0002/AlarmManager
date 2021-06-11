package com.danny.alarmmanager.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import com.danny.alarmmanager.db.AlarmRepository
import com.danny.alarmmanager.utils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //TODO : Send Notification
        val repository = AlarmRepository(context)

        val manager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        CoroutineScope(Dispatchers.IO).launch {
            val alarm = repository.getAlarm(intent.extras?.getLong("code") ?: 0L)
            withContext(Dispatchers.Main) {
                alarm?.let {
                    manager.sendNotification(
                        it.title,
                        it.contents,
                        Color.parseColor(it.color),
                        context
                    )
                    Log.d("TIME", "TIME SET!")
                    repository.deleteAlarm(it)
                }
            }
        }
    }
}