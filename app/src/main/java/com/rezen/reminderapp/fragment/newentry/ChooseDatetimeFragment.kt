package com.rezen.reminderapp.fragment.newentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.rezen.reminderapp.R
import com.rezen.reminderapp.databinding.ChooseDatetimeBinding
import com.rezen.reminderapp.viewmodel.NewEntryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseDatetimeFragment : Fragment() {
    private var _binding: ChooseDatetimeBinding? = null
    private val binding get() = _binding!!
    private val newEntryViewModel: NewEntryViewModel by hiltNavGraphViewModels(R.id.navigation)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ChooseDatetimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            timePicker.apply {
                setIs24HourView(true)
                setOnTimeChangedListener { _, hourOfDay, minute ->
                    newEntryViewModel.setRemindAt(hourOfDay, minute)
                }
            }
            datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
                newEntryViewModel.setRemindAt(year, month + 1, dayOfMonth)
            }
            finishButton.setOnClickListener {
                newEntryViewModel.createEntry {
                    findNavController().navigate(R.id.action_chooseDatetimeFragment_to_listFragment)
                }
            }
        }
    }
}