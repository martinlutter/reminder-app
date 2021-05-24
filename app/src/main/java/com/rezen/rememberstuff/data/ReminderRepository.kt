package com.rezen.rememberstuff.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rezen.rememberstuff.data.dao.ReminderDao
import com.rezen.rememberstuff.data.entity.Reminder
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class ReminderRepository @Inject constructor(private val reminderDao: ReminderDao) {
    suspend fun create(reminder: Reminder) = reminderDao.create(reminder)

    fun getFirstPage(): Flow<PagingData<Reminder>> {
        return Pager(PagingConfig(10)) {
            ReminderPagingSource(reminderDao)
        }.flow
    }

    suspend fun getAll() = reminderDao.getAll()

    suspend fun update(reminder: Reminder) = reminderDao.update(reminder)

    suspend fun deleteById(id: Long) = reminderDao.delete(id)
    suspend fun deleteAll(reminders: List<Reminder>) = reminderDao.deleteAll(reminders)
}