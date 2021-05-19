package com.creamscript.bulychev.views

import androidx.recyclerview.widget.DiffUtil
import com.creamscript.bulychev.models.SimpleContact

object ContactDiffCallback : DiffUtil.ItemCallback<SimpleContact>() {
    override fun areItemsTheSame(oldItem: SimpleContact, newItem: SimpleContact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SimpleContact, newItem: SimpleContact): Boolean {
        return oldItem.contactId == newItem.contactId
    }
}