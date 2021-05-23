package com.rezen.rememberstuff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rezen.rememberstuff.data.ReminderRepository
import com.rezen.rememberstuff.data.entity.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val reminderRepository: ReminderRepository) : ViewModel() {
    fun getFirstPage(): Flow<PagingData<Reminder>> {
        return reminderRepository.getFirstPage().cachedIn(viewModelScope)
    }
}