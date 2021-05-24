package com.rezen.rememberstuff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rezen.rememberstuff.data.ReminderRepository
import com.rezen.rememberstuff.data.entity.Reminder
import com.rezen.rememberstuff.process.ReminderAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val reminderAlarmManager: ReminderAlarmManager
) : ViewModel() {
    fun getFirstPage(): Flow<PagingData<Reminder>> {
        return reminderRepository.getFirstPage().cachedIn(viewModelScope)
    }

    fun deleteSelectedReminders(reminders: List<Reminder>) {
        viewModelScope.launch {
            reminderRepository.deleteAll(reminders)
            reminders.forEach { reminderAlarmManager.removeAlarm(it) }
        }
    }
}