package com.rezen.rememberstuff.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rezen.rememberstuff.data.entity.Reminder

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY remindAt ASC LIMIT :limit OFFSET :offset")
    suspend fun getByPage(limit: Int, offset: Int): List<Reminder>

    @Insert()
    suspend fun create(reminder: Reminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAll(reminders: List<Reminder>)
}