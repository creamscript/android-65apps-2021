package com.creamscript.bulychev

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var serviceDeliverable: ContactService.IService? = null
    private var contactSelectable: ContactSelectable? = null
    private var contactListLayout: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactSelectable)
            contactSelectable = context

        if (context is ContactService.IService) {
            serviceDeliverable = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity)
                ?.supportActionBar
                ?.setTitle(R.string.title_contact_list)

        contactListLayout = view.findViewById(R.id.contactListLayout)
        contactListLayout?.setOnClickListener{
            contactSelectable?.contactSelected("1")
        }
        serviceDeliverable?.getService()?.getContacts(callback)
    }

    private val callback = object : ContactListDeliverable {
        override fun getContactList(list: List<ContactEntity>) {
            if (requireView() != null) {
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

}