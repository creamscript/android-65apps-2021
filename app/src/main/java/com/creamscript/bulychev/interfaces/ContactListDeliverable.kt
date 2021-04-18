package com.creamscript.bulychev.interfaces

import com.creamscript.bulychev.data.Contact

interface ContactListDeliverable {
    fun getContactList(list: List<Contact>)
}