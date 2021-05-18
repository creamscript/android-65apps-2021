package com.creamscript.bulychev.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.creamscript.bulychev.R
import com.creamscript.bulychev.models.SimpleContact

class ContactsAdapter(private val contactSelectable: (SimpleContact) -> Unit) :
    ListAdapter<SimpleContact, ContactsAdapter.ContactViewHolder>(ContactDiffCallback) {

    class ContactViewHolder (itemView: View, val contactSelectable: (SimpleContact) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private var currentSimpleContact: SimpleContact? = null

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    currentSimpleContact?.let {
                        contactSelectable(it)
                    }
                }
            }
        }

        fun bind(simpleContact: SimpleContact) {
            currentSimpleContact = simpleContact
            postContactToList(simpleContact)
        }

        private fun postContactToList(simpleContact: SimpleContact) {
            val contactPhoto = itemView.findViewById<ImageView>(R.id.contactPhoto)
            val contactName = itemView.findViewById<TextView>(R.id.contactName)
            val firstPhone = itemView.findViewById<TextView>(R.id.contactPhoneFirst)

            if (contactName != null) {
                contactPhoto.setImageResource(simpleContact.photoResId)
                contactName.text = simpleContact.contactName
                firstPhone.text = simpleContact.firstPhone
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.contact_item, parent, false)

        return ContactViewHolder(view, contactSelectable)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }
}