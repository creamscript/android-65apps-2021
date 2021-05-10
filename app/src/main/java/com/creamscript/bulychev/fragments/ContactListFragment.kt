package com.creamscript.bulychev.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.creamscript.bulychev.R
import com.creamscript.bulychev.interfaces.ContactListDeliverable
import com.creamscript.bulychev.interfaces.ContactSelectable
import com.creamscript.bulychev.services.ContactService
import com.creamscript.bulychev.data.REQUEST_CODE_READ_CONTACTS
import com.creamscript.bulychev.data.SimpleContact

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var serviceDeliverable: ContactService.IService? = null
    private var contactSelectable: ContactSelectable? = null
    private var contactListLayout: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactSelectable)
            contactSelectable = context

        if (context is ContactService.IService)
            serviceDeliverable = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity)
                .supportActionBar
                ?.setTitle(R.string.title_contact_list)

        contactListLayout = view.findViewById(R.id.contactListLayout)
        contactListLayout?.setOnClickListener {
            contactSelectable?.contactSelected("0")
        }

        val hasReadContactPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            serviceDeliverable?.getService()?.getContacts(callback)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
        }
    }

    private val callback = object : ContactListDeliverable {
        override fun getContactList(list: List<SimpleContact>) {
            requireView().post {
                val contactPhoto = requireView().findViewById<ImageView>(R.id.contactPhoto)
                val contactName = requireView().findViewById<TextView>(R.id.contactName)
                val firstPhone = requireView().findViewById<TextView>(R.id.contactPhoneFirst)

                if (contactName != null) {
                    contactPhoto.setImageDrawable(ContextCompat.getDrawable(requireContext(), list[0].photoResId))
                    contactName.text = list[0].contactName
                    firstPhone.text = list[0].firstPhone
                }
            }
        }
    }

    override fun onDestroyView() {
        contactListLayout = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactSelectable = null
        serviceDeliverable = null
        super.onDetach()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_CONTACTS ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    serviceDeliverable?.getService()?.getContacts(callback)
                } else {
                    Toast.makeText(
                            requireContext(),
                            R.string.msg_deny_permission_contacts,
                            Toast.LENGTH_LONG).show()
                }
        }
    }
}