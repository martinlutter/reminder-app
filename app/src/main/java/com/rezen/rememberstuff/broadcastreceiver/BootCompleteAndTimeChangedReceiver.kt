package com.rezen.rememberstuff.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rezen.rememberstuff.data.ReminderRepository
import com.rezen.rememberstuff.process.ReminderAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootCompleteAndTimeChangedReceiver : BroadcastReceiver(), CoroutineScope by MainScope() {
    @Inject
    lateinit var reminderRepository: ReminderRepository
    @Inject
    lateinit var reminderAlarmManager: ReminderAlarmManager

    override fun onReceive(context: Context, intent: Intent) {
        if (!listOf(Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_TIME_CHANGED).contains(intent.action)) {
            return
        }

        launch {
            val reminders = reminderRepository.getAll()
            reminders
                .filter { it.remindAt > LocalDateTime.now() }
                .forEach { reminderAlarmManager.setAlarm(it) }

            reminderRepository.deleteAll(reminders.filter { it.remindAt <= LocalDateTime.now() })
        }
    }
}