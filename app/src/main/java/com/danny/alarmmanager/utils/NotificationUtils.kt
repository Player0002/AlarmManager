package com.danny.alarmmanager.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.app.NotificationCompat
import com.danny.alarmmanager.MainActivity
import com.danny.alarmmanager.R

private val NOTIFICATION_ID = 0

val fillPaint = Paint()

fun NotificationManager.sendNotification(
    title: String,
    contents: String,
    color: Int,
    context: Context
) {

    val content = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        content,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val map = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(map)
    canvas.drawRect(0f, 0f, 300f, 300f, fillPaint.apply { this.color = Color.TRANSPARENT })
    canvas.drawCircle(150f, 150f, 150f, fillPaint.apply { this.color = color })
    canvas.setBitmap(map)

    val builder = NotificationCompat.Builder(context, "alarm_channel")
        .setSmallIcon(R.drawable.ic_baseline_add_24)
        .setColor(color)
        .setContentTitle(title)
        .setContentText(contents)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setLargeIcon(map)
    notify(NOTIFICATION_ID, builder.build())
}