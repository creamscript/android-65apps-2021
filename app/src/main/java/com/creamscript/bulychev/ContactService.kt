package com.creamscript.bulychev

import android.app.Service
import android.content.Intent
import android.os.*
import java.lang.ref.WeakReference


class ContactService : Service() {

    private val binder = ContactBinder()

    fun getContacts(callback: ContactListDeliverable) {
        val weakReferenceCallback = WeakReference(callback)
        Thread {
            weakReferenceCallback.get()?.getContactList(contacts)
        }.start()
    }

    fun getContact(callback: ContactDetailsDeliverable, id: Int) {
        val weakReferenceCallback = WeakReference(callback)
        Thread {
            weakReferenceCallback.get()?.getContactDetails(contacts[id])
        }.start()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    interface IService {
        fun getService(): ContactService?
    }
}