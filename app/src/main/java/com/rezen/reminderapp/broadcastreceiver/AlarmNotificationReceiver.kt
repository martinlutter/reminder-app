package com.rezen.reminderapp.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rezen.reminderapp.BuildConfig
import com.rezen.reminderapp.broadcastreceiver.notificationaction.BROADCAST_ACTION_COMPLETE_REMINDER
import com.rezen.reminderapp.broadcastreceiver.notificationaction.BROADCAST_ACTION_SNOOZE_REMINDER
import com.rezen.reminderapp.broadcastreceiver.notificationaction.CompleteReceiver
import com.rezen.reminderapp.broadcastreceiver.notificationaction.SnoozeReceiver

const val BROADCAST_ACTION_ALARM = "${BuildConfig.APPLICATION_ID}.action.alarm"

class AlarmNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BROADCAST_ACTION_ALARM) {
            return
        }

        val reminderId = intent.getLongExtra("reminderId", 0)
        val reminderText = intent.getStringExtra("reminderText") ?: return

        NotificationManagerCompat.from(context).notify(
            reminderId.toInt(),
            NotificationCompat.Builder(context, "reminderapp.notification_channel")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText(reminderText)
                .addAction(
                    android.R.drawable.sym_def_app_icon,
                    "Snooze",
                    PendingIntent.getBroadcast(
                        context,
                        reminderId.toInt(),
                        Intent(context, SnoozeReceiver::class.java).apply {
                            putExtra("reminderId", reminderId)
                            action = BROADCAST_ACTION_SNOOZE_REMINDER
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .addAction(
                    android.R.drawable.sym_def_app_icon,
                    "Complete",
                    PendingIntent.getBroadcast(
                        context,
                        reminderId.toInt(),
                        Intent(context, CompleteReceiver::class.java).apply {
                            putExtra("reminderId", reminderId)
                            action = BROADCAST_ACTION_COMPLETE_REMINDER
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .build()
        )
    }
}