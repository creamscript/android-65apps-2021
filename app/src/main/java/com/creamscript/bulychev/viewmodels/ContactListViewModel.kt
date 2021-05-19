package com.creamscript.bulychev.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.creamscript.bulychev.ContactRepository
import com.creamscript.bulychev.models.SimpleContact

class ContactListViewModel: ViewModel() {
    private val contactRepository = ContactRepository()
    private val contactList = MutableLiveData<List<SimpleContact>>()

    fun getContactList(): LiveData<List<SimpleContact>> {
        return contactList
    }

    fun loadContactList(context: Context, query: String?) {
        Thread {
            contactList.postValue(contactRepository.getContacts(context, query))
        }.start()
    }
}