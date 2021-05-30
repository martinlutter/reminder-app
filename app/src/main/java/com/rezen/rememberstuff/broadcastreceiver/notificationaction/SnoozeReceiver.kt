package com.rezen.rememberstuff.broadcastreceiver.notificationaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.rezen.rememberstuff.data.ReminderRepository
import com.rezen.rememberstuff.process.ReminderAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SnoozeReceiver : BroadcastReceiver(), CoroutineScope {
    @Inject
    lateinit var reminderRepository: ReminderRepository
    @Inject
    lateinit var reminderAlarmManager: ReminderAlarmManager

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("reminderId", 0)
        val pendingResult = goAsync()
        Log.d("blabla", "SnoozeReceiver:onReceive, id: $reminderId")

        launch {
            reminderRepository.updateRemindAt(reminderId, LocalDateTime.now().plusMinutes(10))
            reminderAlarmManager.setAlarm(reminderRepository.get(reminderId))

            NotificationManagerCompat.from(context).cancel(reminderId.toInt())

            pendingResult.finish()
        }
    }
}