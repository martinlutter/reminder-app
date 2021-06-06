package com.rezen.reminderapp.broadcastreceiver.notificationaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rezen.reminderapp.BuildConfig
import com.rezen.reminderapp.data.ReminderRepository
import com.rezen.reminderapp.fragment.BROADCAST_ACTION_REFRESH_LIST
import com.rezen.reminderapp.process.ReminderAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val BROADCAST_ACTION_SNOOZE_REMINDER = "${BuildConfig.APPLICATION_ID}.action.snooze_reminder"

@AndroidEntryPoint
class SnoozeReceiver : BroadcastReceiver(), CoroutineScope {
    @Inject
    lateinit var reminderRepository: ReminderRepository
    @Inject
    lateinit var reminderAlarmManager: ReminderAlarmManager

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BROADCAST_ACTION_SNOOZE_REMINDER) {
            return
        }

        val reminderId = intent.getLongExtra("reminderId", 0)
        val pendingResult = goAsync()
        Log.d("blabla", "SnoozeReceiver:onReceive, id: $reminderId")

        launch {
            reminderRepository.updateRemindAt(reminderId, LocalDateTime.now().plusMinutes(10))
            reminderAlarmManager.setAlarm(reminderRepository.get(reminderId))
            NotificationManagerCompat.from(context).cancel(reminderId.toInt())

            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(BROADCAST_ACTION_REFRESH_LIST))

            pendingResult.finish()
        }
    }
}