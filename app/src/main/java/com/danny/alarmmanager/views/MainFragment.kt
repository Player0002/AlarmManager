package com.danny.alarmmanager.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.danny.alarmmanager.R
import com.danny.alarmmanager.adapter.AlarmAdapter
import com.danny.alarmmanager.adapter.AlarmListener
import com.danny.alarmmanager.databinding.FragmentMainBinding
import com.danny.alarmmanager.db.AlarmDatabase
import com.danny.alarmmanager.models.AlarmItem
import com.danny.alarmmanager.viewmodels.MainViewModel
import com.danny.alarmmanager.viewmodels.MainViewModelFactory

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val database by lazy { AlarmDatabase.getInstance(requireActivity().application) }
    private val factory by lazy {
        MainViewModelFactory(
            requireActivity().application,
            database.getDao()
        )
    }

    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<MainViewModel> { factory }
        alarmAdapter = AlarmAdapter(object : AlarmListener {
            override fun onAlarmChanged(alarm: AlarmItem, boolean: Boolean) {
                viewModel.updateItem(alarm.apply { enabled = boolean })
            }
        })

        binding.floating.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_titleFragment)
        }

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteItem(alarmAdapter.list.currentList[viewHolder.adapterPosition])
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.recycler)

        binding.recycler.adapter = alarmAdapter
        createChannel(
            "alarm_channel",
            "Alarm"
        )

        viewModel.items.observe(viewLifecycleOwner) {
            alarmAdapter.list.submitList(it)
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}