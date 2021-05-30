package com.rezen.rememberstuff.broadcastreceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rezen.rememberstuff.AlarmActivity
import com.rezen.rememberstuff.broadcastreceiver.notificationaction.CompleteReceiver
import com.rezen.rememberstuff.broadcastreceiver.notificationaction.SnoozeReceiver
import com.rezen.rememberstuff.data.entity.Reminder

class AlarmNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("reminderId", 0)
        val reminderText = intent.getStringExtra("reminderText") ?: return
        Log.d("blabla", "AlarmNotificationReceiver:onReceive, id: $reminderId, text: $reminderText")

        NotificationManagerCompat.from(context).notify(
            reminderId.toInt(),
            NotificationCompat.Builder(context, "rememberstuff.notification_channel")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText(reminderText)
//                .setFullScreenIntent(createIntent(context, reminderId), true)
//                .setContentIntent(createIntent(context, reminderId))
                .addAction(
                    android.R.drawable.sym_def_app_icon,
                    "Snooze",
                    PendingIntent.getBroadcast(
                        context,
                        reminderId.toInt(),
                        Intent(context, SnoozeReceiver::class.java).apply {
                            putExtra("reminderId", reminderId)
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
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .build()
        )
    }

    private fun createIntent(context: Context, reminderId: Long) = PendingIntent.getActivity(
        context,
        reminderId.toInt(),
        Intent(context, AlarmActivity::class.java).apply {
            putExtra("reminderId", reminderId)
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}