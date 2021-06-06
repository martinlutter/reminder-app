package com.rezen.reminderapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rezen.reminderapp.data.dao.ReminderDao
import com.rezen.reminderapp.data.entity.Reminder

class ReminderPagingSource(private val reminderDao: ReminderDao) : PagingSource<Int, Reminder>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Reminder> {
        val page = params.key ?: 1
        val loadSize = 10
        val data = reminderDao.getByPage(loadSize, (page - 1) * loadSize)

        return try {
            Log.d("blabla", "loading page $page, loadSize $loadSize")
            LoadResult.Page(
                data,
                null,
                if (data.count() == loadSize) page + 1 else null
            )
        } catch (error: Error) {
            LoadResult.Error(error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Reminder>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}