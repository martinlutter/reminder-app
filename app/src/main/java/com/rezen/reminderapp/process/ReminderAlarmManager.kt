package com.rezen.reminderapp.process

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rezen.reminderapp.broadcastreceiver.AlarmNotificationReceiver
import com.rezen.reminderapp.broadcastreceiver.BROADCAST_ACTION_ALARM
import com.rezen.reminderapp.data.entity.Reminder
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneOffset
import javax.inject.Inject

@Reusable
class ReminderAlarmManager @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) {
    fun setAlarm(reminder: Reminder) {
        val atZone = reminder.remindAt.withSecond(0).atZone(ZoneOffset.systemDefault())
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            atZone.toEpochSecond() * 1000,
            createIntent(reminder)
        )
    }

    fun removeAlarm(reminder: Reminder) {
        alarmManager.cancel(createIntent(reminder))
    }

    private fun createIntent(reminder: Reminder) = PendingIntent.getBroadcast(
        context,
        reminder.id.toInt(),
        Intent(context, AlarmNotificationReceiver::class.java).apply {
            putExtra("reminderId", reminder.id)
            putExtra("reminderText", reminder.text)
            action = BROADCAST_ACTION_ALARM
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}