package com.rezen.reminderapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rezen.reminderapp.data.dao.ReminderDao
import com.rezen.reminderapp.data.entity.Reminder
import com.rezen.reminderapp.data.typeconverter.LocalDateTimeConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import kotlin.random.Random

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
//            context.deleteDatabase("reminders")
            return Room.databaseBuilder(context, AppDatabase::class.java, "reminders")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
//                        Log.d("blabla", "database onopen")
//                        GlobalScope.launch(Dispatchers.IO) {
//                            getInstance(context).reminderDao().createAll(fixtureReminders().take(20).toList())
//                        }
                    }
                })
                .build()
        }

        private fun fixtureReminders(): Flow<Reminder> = flow {
            var id = 1.toLong()
            while (true) {
                emit(
                    Reminder(
                        Random.nextBytes(32).toString(),
                        LocalDateTime.of(
                            2021,
                            Random.nextInt(12),
                            Random.nextInt(31),
                            Random.nextInt(23),
                            Random.nextInt(59),
                            0
                        )
                    ).apply { this.id = id; ++id }
                )
            }
        }
    }
}