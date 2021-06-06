package com.rezen.reminderapp.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ReminderItemDetailsLookup(private val view: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val childView = view.findChildViewUnder(e.x, e.y) ?: return null
        val childViewHolder = view.getChildViewHolder(childView)

        return (childViewHolder as ReminderViewHolder).getItemDetails()
    }
}