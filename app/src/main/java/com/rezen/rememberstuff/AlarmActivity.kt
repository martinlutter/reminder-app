package com.rezen.rememberstuff

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rezen.rememberstuff.data.ReminderRepository
import com.rezen.rememberstuff.data.entity.Reminder
import com.rezen.rememberstuff.databinding.ActivityAlarmBinding
import com.rezen.rememberstuff.process.ReminderAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    @Inject
    lateinit var reminderRepository: ReminderRepository
    @Inject
    lateinit var reminderAlarmManager: ReminderAlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent == null) {
            return
        }

        //todo: still need to use PowerManager/WakeLock/Intent.FLAG_ACTIVITY_REORDER_TO_FRONT to put activity to foreground
        //todo: make reminder serializable and unserialize it as a whole
        val reminderText = intent.getStringExtra("remindText") ?: ""
        val remindAt = intent.getSerializableExtra("remindAt") as LocalDateTime
        val id = intent.getLongExtra("id", 0)

        binding.apply {
            alarm.text = reminderText
            dateTime.text = remindAt.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            )
            snooze.setOnClickListener {
                lifecycleScope.launch {
                    val reminder = Reminder(reminderText, remindAt.plusMinutes(10)).apply { this.id = id }
                    reminderRepository.update(reminder)
                    reminderAlarmManager.setAlarm(reminder)
                    finish()
                }
            }
            complete.setOnClickListener {
                lifecycleScope.launch {
                    reminderRepository.deleteById(id)
                    finish()
                }
            }
        }
    }
}