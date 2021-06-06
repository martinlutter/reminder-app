package com.rezen.reminderapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezen.reminderapp.data.ReminderRepository
import com.rezen.reminderapp.data.entity.Reminder
import com.rezen.reminderapp.process.ReminderAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reminderRepository: ReminderRepository,
    private val reminderAlarmManager: ReminderAlarmManager
) : ViewModel() {
    private val reminderText = savedStateHandle.getLiveData<String>("reminderText", "")
    private val remindAt = savedStateHandle.getLiveData<LocalDateTime>("remindAt", LocalDateTime.now())

    fun setReminderText(text: String) {
        reminderText.value = text
    }

    fun getReminderText(): LiveData<String> = reminderText

    fun setRemindAt(hour: Int, minute: Int) {
        remindAt.value?.apply {
            remindAt.value = withHour(hour).withMinute(minute)
        }
    }

    fun setRemindAt(year: Int, month: Int, dayOfMonth: Int) {
        remindAt.value?.apply {
            remindAt.value = withYear(year).withMonth(month).withDayOfMonth(dayOfMonth)
        }
    }

    fun createEntry(onFinished: () -> Unit) {
        val reminder = Reminder(reminderText.value!!, remindAt.value!!)
        viewModelScope.launch {
            reminder.id = reminderRepository.create(reminder)
            reminderAlarmManager.setAlarm(reminder)

            reminderText.value = ""
            remindAt.value = LocalDateTime.now()
            onFinished()
        }
    }
}