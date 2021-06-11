package com.danny.alarmmanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.danny.alarmmanager.R
import com.danny.alarmmanager.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    lateinit var binding: FragmentTitleBinding

    private val args: TitleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTitleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.title.setText(args.title)
        binding.contents.setText(args.contents)

        binding.next.setOnClickListener {
            val title = binding.title.text.toString()
            val contents = binding.contents.text.toString()
            when {
                title.isBlank() -> emptyTxt("title $title")
                contents.isBlank() -> emptyTxt("contents $contents")
                else -> nextFragment(title, contents)
            }
        }

    }

    private fun nextFragment(title: String, contents: String) {
        findNavController().navigate(
            R.id.action_titleFragment_to_addAlarmFragment,
            bundleOf("title" to title, "contents" to contents)
        )
    }

    private fun emptyTxt(type: String) {
        binding.next.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Cannot set $type null",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}