package com.creamscript.bulychev.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.creamscript.bulychev.ContactRepository
import com.creamscript.bulychev.models.Contact

class ContactDetailsViewModel : ViewModel() {
    private val contactRepository = ContactRepository()
    private val contact = MutableLiveData<Contact>()

    fun getContact() : LiveData<Contact>  {
        return contact
    }

    fun loadContact(context: Context, id: String){
        Thread {
            contact.postValue(contactRepository.getContact(context, id))
        }.start()
    }
}