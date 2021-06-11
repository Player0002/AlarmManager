package com.danny.alarmmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.danny.alarmmanager.databinding.AlarmItemBinding
import com.danny.alarmmanager.models.AlarmItem

interface AlarmListener {
    fun onAlarmChanged(alarm: AlarmItem, boolean: Boolean)
}

class AlarmAdapter(private val listener: AlarmListener) :
    RecyclerView.Adapter<AlarmAdapter.AlarmHolder>() {
    inner class AlarmHolder(private val binding: AlarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarmItem: AlarmItem) {
            binding.item = alarmItem
            binding.alarmSwitch.setOnCheckedChangeListener { _, b ->
                listener.onAlarmChanged(
                    alarmItem,
                    b
                )
            }
        }
    }

    val list = AsyncListDiffer(this, AlarmItem.differ)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = AlarmItemBinding.inflate(inflater, parent, false)
        return AlarmHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        holder.bind(list.currentList[position])
    }

    override fun getItemCount(): Int = list.currentList.size
}