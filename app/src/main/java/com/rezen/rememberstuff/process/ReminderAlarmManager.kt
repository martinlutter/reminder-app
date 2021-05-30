package com.rezen.rememberstuff.process

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rezen.rememberstuff.broadcastreceiver.AlarmNotificationReceiver
import com.rezen.rememberstuff.broadcastreceiver.BootCompleteAndTimeChangedReceiver
import com.rezen.rememberstuff.data.entity.Reminder
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
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
//            LocalDateTime.now().plusSeconds(5).atZone(ZoneOffset.systemDefault()).toEpochSecond() * 1000,
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
            action = "tralalacik"
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}