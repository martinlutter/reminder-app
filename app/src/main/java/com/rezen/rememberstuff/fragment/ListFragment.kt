package com.rezen.rememberstuff.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.rezen.rememberstuff.R
import com.rezen.rememberstuff.adapter.ReminderAdapter
import com.rezen.rememberstuff.adapter.ReminderItemDetailsLookup
import com.rezen.rememberstuff.databinding.ListBinding
import com.rezen.rememberstuff.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: ListBinding? = null
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by hiltNavGraphViewModels(R.id.navigation)
    private var selectionTracker: SelectionTracker<Long>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reminderAdapter = ReminderAdapter()
        binding.apply {
            reminderList.adapter = reminderAdapter
            newReminderButton.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_recordVoiceFragment)
            }
        }

        val keyProvider = reminderAdapter.ReminderItemKeyProvider()
        val selectionTracker = SelectionTracker.Builder(
            "reminder-list-tracker",
            binding.reminderList,
            keyProvider,
            ReminderItemDetailsLookup(binding.reminderList),
            StorageStrategy.createLongStorage()
        ).build()

        reminderAdapter.setTracker(selectionTracker)
        selectionTracker.addObserver(
            ReminderAdapter.SelectionTrackerObserver(
                binding.reminderList,
                keyProvider,
                selectionTracker
            ) { requireActivity().startActionMode(it) }
        )

        this.selectionTracker = selectionTracker

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.getFirstPage().collectLatest {
                reminderAdapter.submitData(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectionTracker?.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        selectionTracker?.onRestoreInstanceState(savedInstanceState)
    }
}