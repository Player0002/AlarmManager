package com.danny.alarmmanager.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "alarm_table")
data class AlarmItem(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val contents: String,
    val time: Long,
    var enabled: Boolean = false,
    val color: String = "#000000"// HEX
) : Serializable {

    @Ignore
    var calendar: Calendar = (Calendar.getInstance().clone() as Calendar).apply {
        timeInMillis = this@AlarmItem.time

    }

    val timeStr: String
        get() = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
    val dateStr: String
        get() = "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DAY_OF_MONTH)}"

    companion object {
        val differ = object : DiffUtil.ItemCallback<AlarmItem>() {
            override fun areItemsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlarmItem, newItem: AlarmItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}