package com.rezen.reminderapp

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : AppCompatActivity() {
//    @Inject
//    lateinit var reminderRepository: ReminderRepository
//    @Inject
//    lateinit var reminderAlarmManager: ReminderAlarmManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityAlarmBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        val reminder = intent?.getParcelableExtra<Reminder>("reminder") ?: return
//
//        NotificationManagerCompat.from(this).cancel(reminder.id.toInt())
//
//        binding.apply {
//            alarm.text = reminder.text
//            dateTime.text = reminder.remindAt.format(
//                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
//            )
//            snooze.setOnClickListener {
//                lifecycleScope.launch {
//                    reminder.remindAt = reminder.remindAt.plusMinutes(10)
//                    reminderRepository.updateRemindAt(reminder)
//                    reminderAlarmManager.setAlarm(reminder)
//                    finish()
//                }
//            }
//            complete.setOnClickListener {
//                lifecycleScope.launch {
//                    reminderRepository.deleteById(reminder.id)
//                    finish()
//                }
//            }
//        }
//    }
}