package com.creamscript.bulychev

import android.net.Uri
import android.provider.ContactsContract
import android.content.Context
import com.creamscript.bulychev.models.Contact
import com.creamscript.bulychev.models.SimpleContact
import io.reactivex.rxjava3.core.Single

//import java.util.concurrent.TimeUnit

private const val PHONE = "PHONE"
private const val EMAIL = "EMAIL"
private const val DESCRIPTION = "DESCRIPTION"
private const val BIRTHDAY = "BIRTHDAY"

class ContactRepository {

    fun getContacts(context: Context, query: String?): Single<List<SimpleContact>> =
        Single.fromCallable { getContactsByQuery(context, query) }

    fun getContact(context: Context, id: String): Single<Contact> =
        Single.fromCallable { getContactById(context, id) }

    private fun getContactsByQuery(context: Context, query: String?): List<SimpleContact> {

        //TimeUnit.SECONDS.sleep(1); // mock загрузки

        val contactList: MutableList<SimpleContact> = mutableListOf()
        var selectionParamContacts: String? = null

        if (query != null) {
            selectionParamContacts = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE \'%" + query + "%\'"
        }

        val cursor = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                selectionParamContacts,
                null,
                null)

        cursor.use { cursor ->
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactList.add(
                        SimpleContact(
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                            getContactContact(context, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)), PHONE, 0),
                            R.drawable.duckduckgo
                        )
                    )
                }
            }
        }

        return contactList
    }

    private fun getContactById(context: Context, id: String) : Contact {

        //TimeUnit.SECONDS.sleep(1); // mock загрузки

        var contact = Contact("0", "", "","",
            "","","",
            R.drawable.ic_android_black_96dp, ""
        )

        val cursor = context.contentResolver.query(
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
                        getContactContact(context, id, PHONE, 0),
                        getContactContact(context, id, PHONE, 1),
                        getContactContact(context, id, EMAIL, 0),
                        getContactContact(context, id, EMAIL, 1),
                        getContactContact(context, id, DESCRIPTION, 0),
                        R.drawable.duckduckgo,
                        getContactContact(context, id, BIRTHDAY, 0)
                    )
                }
            }
        }

        return contact
    }

    private fun getContactContact(context: Context, id: String, field: String, num: Int): String {
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

        val cursor = context.contentResolver.query(
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

}