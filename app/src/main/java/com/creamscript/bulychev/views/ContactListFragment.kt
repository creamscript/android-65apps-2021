package com.creamscript.bulychev.views

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
import androidx.lifecycle.ViewModelProvider
import com.creamscript.bulychev.R
import com.creamscript.bulychev.REQUEST_CODE_READ_CONTACTS
import com.creamscript.bulychev.models.SimpleContact
import com.creamscript.bulychev.viewmodels.ContactListViewModel

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var contactListViewModel: ContactListViewModel? = null
    private var contactSelectable: ContactSelectable? = null
    private var contactListLayout: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactSelectable)
            contactSelectable = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactListViewModel = ViewModelProvider(this).get(ContactListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity)
                .supportActionBar
                ?.setTitle(R.string.title_contact_list)

        contactListLayout = view.findViewById(R.id.contactListLayout)
        contactListLayout?.setOnClickListener {
            contactSelectable?.contactSelected("1")
        }

        val hasReadContactPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            contactListViewModel?.loadContactList(requireContext())
            contactListViewModel
                ?.getContactList()
                ?.observe(viewLifecycleOwner, { postContactToList(it[0]) })

        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
        }
    }

    override fun onDestroyView() {
        contactListLayout = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactSelectable = null
        super.onDetach()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_CONTACTS ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactListViewModel?.loadContactList(requireContext())
                    contactListViewModel
                        ?.getContactList()
                        ?.observe(viewLifecycleOwner, { postContactToList(it[0]) })

                } else {
                    Toast.makeText(
                            requireContext(),
                            R.string.msg_deny_permission_contacts,
                            Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun postContactToList(simpleContact: SimpleContact) {
        val contactPhoto = requireView().findViewById<ImageView>(R.id.contactPhoto)
        val contactName = requireView().findViewById<TextView>(R.id.contactName)
        val firstPhone = requireView().findViewById<TextView>(R.id.contactPhoneFirst)

        if (contactName != null) {
            contactPhoto.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    simpleContact.photoResId
                )
            )
            contactName.text = simpleContact.contactName
            firstPhone.text = simpleContact.firstPhone
        }
    }
}