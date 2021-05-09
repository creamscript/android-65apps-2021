package com.creamscript.bulychev.interfaces

import com.creamscript.bulychev.data.SimpleContact

interface ContactListDeliverable {
    fun getContactList(list: List<SimpleContact>)
}