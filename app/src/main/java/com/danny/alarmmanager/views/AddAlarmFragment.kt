package com.danny.alarmmanager.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.danny.alarmmanager.databinding.FragmentAddAlarmBinding
import com.danny.alarmmanager.db.AlarmDatabase
import com.danny.alarmmanager.viewmodels.AddAlarmViewModel
import com.danny.alarmmanager.viewmodels.AddAlarmViewModelFactory
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class AddAlarmFragment : Fragment() {

    lateinit var binding: FragmentAddAlarmBinding

    private val database by lazy { AlarmDatabase.getInstance(requireActivity().application) }

    private val factory by lazy{ AddAlarmViewModelFactory(requireActivity().application, database.getDao()) }


    private val args: AddAlarmFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAlarmBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: AddAlarmViewModel by viewModels { factory }
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.dateTime.setOnClickListener {
            val dialog = DatePickerDialog(requireContext())
            dialog.setOnDateSetListener { _, y, m, d ->
                viewModel.updateTime(y, m, d)
            }
            dialog.show()
        }

        binding.timePicker.setOnTimeChangedListener { _, h, m ->
            viewModel.updateHour(h, m)
        }

        binding.colorBox.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setPositiveButton("설정", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.hexCode?.let { color -> viewModel.updateColor("#$color") }
                    }
                })
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show()
        }

        binding.submit.setOnClickListener {
            viewModel.saveAlarm(args.title, args.contents)
            close()
        }

    }

    private fun close() {
        findNavController().navigateUp()
        findNavController().navigateUp()
    }

}