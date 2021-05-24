package com.rezen.rememberstuff.adapter

import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.rezen.rememberstuff.data.entity.Reminder
import com.rezen.rememberstuff.databinding.ReminderListItemBinding
import java.time.format.DateTimeFormatter

class ReminderViewHolder(private val binding: ReminderListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private var reminderId: Long? = null

    fun bind(reminder: Reminder, selected: Boolean) {
        reminderId = reminder.id

        binding.apply {
            root.isActivated = selected
            reminderText.text = reminder.text
            remindAt.text = reminder.remindAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        }
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
        return object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int {
                return bindingAdapterPosition
            }

            override fun getSelectionKey(): Long? {
                return reminderId
            }
        }
    }
}