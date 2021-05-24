package com.rezen.rememberstuff.adapter

import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rezen.rememberstuff.R
import com.rezen.rememberstuff.data.entity.Reminder
import com.rezen.rememberstuff.databinding.ReminderListItemBinding
import java.time.format.DateTimeFormatter

class ReminderAdapter : PagingDataAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {
    private lateinit var selectionTracker: SelectionTracker<Long>
    fun setTracker(tracker: SelectionTracker<Long>) {
        selectionTracker = tracker
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        getItem(position)?.let { reminder ->
            holder.bind(reminder, selectionTracker.isSelected(reminder.id))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder(
            ReminderListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ReminderItemKeyProvider : ItemKeyProvider<Long>(SCOPE_CACHED) {
        override fun getKey(position: Int): Long {
            return snapshot().items[position].id
        }

        override fun getPosition(key: Long): Int {
            return snapshot().items.indexOfFirst { it.id == key }
        }
    }

    inner class SelectionTrackerObserver(
        private var recyclerView: RecyclerView,
        private val keyProvider: ItemKeyProvider<Long>,
        private val startActionMode: (ActionMode.Callback) -> ActionMode?,
        private val deleteReminders: (List<Reminder>) -> Unit
    ) : SelectionTracker.SelectionObserver<Long>() {
        private var actionMode: ActionMode? = null

        override fun onItemStateChanged(key: Long, selected: Boolean) {
            recyclerView.findViewHolderForAdapterPosition(keyProvider.getPosition(key))
                ?.let { viewHolder ->
                    viewHolder.itemView.isActivated = selected
                }

            if (!selectionTracker.hasSelection()) {
                actionMode?.finish()
            }
            actionMode?.title = selectionTracker.selection.size().toString() + " selected"
        }

        override fun onSelectionChanged() {
            if (actionMode != null || !selectionTracker.hasSelection()) {
                return
            }

            actionMode = startActionMode(object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                    mode.menuInflater.inflate(R.menu.reminder_list_selection, menu)
                    mode.title = selectionTracker.selection.size().toString() + " selected"
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                    return when (item.itemId) {
                        R.id.delete -> {
                            deleteReminders(
                                selectionTracker.selection.mapNotNull { selectionKey ->
                                    getItem(keyProvider.getPosition(selectionKey))
                                }
                            )
                            actionMode?.finish()
                            refresh()
                            true
                        }
                        else -> false
                    }
                }

                override fun onDestroyActionMode(mode: ActionMode) {
                    actionMode = null
                    selectionTracker.clearSelection()
                }
            })
        }

        override fun onSelectionRestored() {
            onSelectionChanged()
        }
    }
}

private class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return oldItem == newItem
    }
}

class ReminderItemDetailsLookup(private val view: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val childView = view.findChildViewUnder(e.x, e.y) ?: return null
        val childViewHolder = view.getChildViewHolder(childView)

        return (childViewHolder as ReminderViewHolder).getItemDetails()
    }
}