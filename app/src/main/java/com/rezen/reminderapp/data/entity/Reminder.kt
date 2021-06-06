package com.rezen.reminderapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "reminder")
data class Reminder(
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "remindAt") val remindAt: LocalDateTime
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}