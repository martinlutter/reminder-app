package com.rezen.reminderapp.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.rezen.reminderapp.BuildConfig
import com.rezen.reminderapp.R
import com.rezen.reminderapp.adapter.ReminderAdapter
import com.rezen.reminderapp.adapter.ReminderItemDetailsLookup
import com.rezen.reminderapp.databinding.ListBinding
import com.rezen.reminderapp.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val BROADCAST_ACTION_REFRESH_LIST = "${BuildConfig.APPLICATION_ID}.action.refresh_list"

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: ListBinding? = null
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by hiltNavGraphViewModels(R.id.navigation)
    private var selectionTracker: SelectionTracker<Long>? = null
    private var refreshListReceiver: BroadcastReceiver? = null

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
            reminderAdapter.SelectionTrackerObserver(
                binding.reminderList,
                keyProvider,
                { requireActivity().startActionMode(it) },
                { listViewModel.deleteSelectedReminders(it) }
            )
        )
        selectionTracker.onRestoreInstanceState(savedInstanceState)

        this.selectionTracker = selectionTracker

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.getFirstPage().collectLatest {
                reminderAdapter.submitData(it)
            }
        }

        refreshListReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != BROADCAST_ACTION_REFRESH_LIST) {
                    return
                }

                refreshList()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        refreshListReceiver?.let {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                it, IntentFilter(BROADCAST_ACTION_REFRESH_LIST)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        refreshListReceiver?.let {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectionTracker?.onSaveInstanceState(outState)
    }

    private fun refreshList() {
        (binding.reminderList.adapter as PagingDataAdapter<*, *>?)?.refresh()
    }
}
