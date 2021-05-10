package com.creamscript.bulychev.services

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract
import com.creamscript.bulychev.R
import com.creamscript.bulychev.data.Contact
import com.creamscript.bulychev.data.SimpleContact
import com.creamscript.bulychev.interfaces.ContactDetailsDeliverable
import com.creamscript.bulychev.interfaces.ContactListDeliverable
import java.lang.ref.WeakReference
import java.util.*

private const val PHONE = "PHONE"
private const val EMAIL = "EMAIL"
private const val DESCRIPTION = "DESCRIPTION"
private const val BIRTHDAY = "BIRTHDAY"

class ContactService : Service() {

    private val binder = ContactBinder()

    fun getContacts(callback: ContactListDeliverable) {
        val weakReferenceCallback = WeakReference(callback)
        Thread {
            weakReferenceCallback.get()?.getContactList(getContacts())
        }.start()
    }

    fun getContact(callback: ContactDetailsDeliverable, id: String) {
        val weakReferenceCallback = WeakReference(callback)
        Thread {
            weakReferenceCallback.get()?.getContactDetails(getContact(id))
        }.start()
    }

    private fun getContacts() : List<SimpleContact> {
        val contactList: MutableList<SimpleContact> = mutableListOf()
        val cursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        null)

        cursor.use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactList.add(SimpleContact(
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                            getContactContact(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)), PHONE, 0),
                            R.drawable.duckduckgo
                    ))
                }
            }
        }

        return contactList
    }

    private fun getContact(id: String) : Contact {
        var contact = Contact("0", "", "","",
                "","","",
                R.drawable.ic_android_black_96dp, ""
        )

        val cursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        ContactsContract.Contacts._ID + " = ?",
                        arrayOf(id),
                        null)

        cursor.use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contact = Contact(
                        id,
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                        getContactContact(id, PHONE, 0),
                        getContactContact(id, PHONE, 1),
                        getContactContact(id, EMAIL, 0),
                        getContactContact(id, EMAIL, 1),
                        getContactContact(id, DESCRIPTION, 0),
                        R.drawable.duckduckgo,
                        getContactContact(id, BIRTHDAY, 0)
                    )
                }
            }
        }

        return contact
    }

    private fun getContactContact(id: String, field: String, num: Int): String {
        var result = ""
        var countNum = 0

        val uriParam: Uri
        val selectionParam: String
        val columnData: String
        val selectionArgsParam: Array<String>

        when (field) {
            PHONE -> {
                uriParam = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                selectionParam = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                columnData = ContactsContract.CommonDataKinds.Phone.DATA
                selectionArgsParam = arrayOf(id)
            }
            EMAIL -> {
                uriParam = ContactsContract.CommonDataKinds.Email.CONTENT_URI
                selectionParam = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?"
                columnData = ContactsContract.CommonDataKinds.Email.DATA
                selectionArgsParam = arrayOf(id)
            }
            DESCRIPTION -> {
                uriParam = ContactsContract.Data.CONTENT_URI
                selectionParam = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ?"
                columnData = ContactsContract.CommonDataKinds.Note.NOTE
                selectionArgsParam = arrayOf(id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
            }
            BIRTHDAY -> {
                uriParam = ContactsContract.Data.CONTENT_URI
                selectionParam = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
                columnData = ContactsContract.CommonDataKinds.Event.START_DATE
                selectionArgsParam = arrayOf(id, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
            }
            else -> {
                return result
            }
        }

        val cursor = contentResolver.query(
                        uriParam,
                        null,
                        selectionParam,
                        selectionArgsParam,
                        null
        )

        cursor.use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (num == countNum) {
                        result = cursor.getString(cursor.getColumnIndex(columnData))
                    }
                    countNum++
                }
            }
        }

        return result
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