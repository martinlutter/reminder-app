package com.rezen.rememberstuff.data.dao

import androidx.room.*
import com.rezen.rememberstuff.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY remindAt ASC LIMIT :limit OFFSET :offset")
    suspend fun getByPage(limit: Int, offset: Int): List<Reminder> //todo: try :PagingSource<Long, Reminder>

    @Insert()
    suspend fun create(reminder: Reminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAll(reminders: List<Reminder>)

    @Delete
    suspend fun deleteAll(reminders: List<Reminder>)

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM reminder")
    suspend fun getAll(): List<Reminder>

    @Update
    suspend fun update(reminder: Reminder)
}