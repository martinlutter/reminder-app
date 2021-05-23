package com.rezen.rememberstuff.process

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rezen.rememberstuff.AlarmActivity
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
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            /*reminder.remindAt*/
            LocalDateTime.now().plusSeconds(5).atZone(ZoneOffset.systemDefault()).toEpochSecond(),
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, AlarmActivity::class.java).apply {
                    putExtra("remindText", reminder.text)
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }
}