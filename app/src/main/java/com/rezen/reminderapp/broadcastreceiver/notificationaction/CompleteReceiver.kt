package com.rezen.reminderapp.broadcastreceiver.notificationaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rezen.reminderapp.BuildConfig
import com.rezen.reminderapp.data.ReminderRepository
import com.rezen.reminderapp.fragment.BROADCAST_ACTION_REFRESH_LIST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val BROADCAST_ACTION_COMPLETE_REMINDER = "${BuildConfig.APPLICATION_ID}.action.complete_reminder"

@AndroidEntryPoint
class CompleteReceiver : BroadcastReceiver(), CoroutineScope {
    @Inject
    lateinit var reminderRepository: ReminderRepository

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BROADCAST_ACTION_COMPLETE_REMINDER) {
            return
        }

        val reminderId = intent.getLongExtra("reminderId", 0)
        val pendingResult = goAsync()
        Log.d("blabla", "CompleteReceiver:onReceive, id: $reminderId")

        launch {
            reminderRepository.deleteById(reminderId)
            NotificationManagerCompat.from(context).cancel(reminderId.toInt())

            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(BROADCAST_ACTION_REFRESH_LIST))

            pendingResult.finish()
        }
    }
}