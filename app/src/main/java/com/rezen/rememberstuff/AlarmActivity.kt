package com.rezen.rememberstuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rezen.rememberstuff.data.ReminderRepository
import com.rezen.rememberstuff.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
    @Inject
    lateinit var reminderRepository: ReminderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent == null) {
            return
        }

        binding.alarm.text = intent.getStringExtra("remindText") ?: "no savedState"
        binding.dateTime.text = (intent.getSerializableExtra("remindAt") as LocalDateTime).format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        )
        lifecycleScope.launch { reminderRepository.deleteById(intent.getLongExtra("id", 0)) }
    }
}