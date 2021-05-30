package com.rezen.rememberstuff.broadcastreceiver.notificationaction

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.rezen.rememberstuff.data.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class CompleteReceiver : BroadcastReceiver(), CoroutineScope {
    @Inject
    lateinit var reminderRepository: ReminderRepository

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("reminderId", 0)
        val pendingResult = goAsync()
        Log.d("blabla", "CompleteReceiver:onReceive, id: $reminderId")

        launch {
            reminderRepository.deleteById(reminderId)
            NotificationManagerCompat.from(context).cancel(reminderId.toInt())

            pendingResult.finish()
        }
    }
}