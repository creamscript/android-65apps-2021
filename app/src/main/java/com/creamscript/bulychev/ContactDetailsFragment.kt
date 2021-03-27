package com.creamscript.bulychev

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private var serviceDeliverable: ContactService.IService? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ContactService.IService) {
            serviceDeliverable = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity)
                ?.supportActionBar
                ?.setTitle(R.string.title_contact_details)

        serviceDeliverable?.getService()?.getContact(callback, 0)
    }

    override fun onDetach() {
        serviceDeliverable = null
        super.onDetach()
    }

    private val callback = object : ContactDetailsDeliverable {
        override fun getContactDetails(contact: ContactEntity) {
            if (requireView() != null) {
                requireView().post {
                    val contactPhoto = requireView().findViewById<ImageView>(R.id.contactDetailsPhoto)
                    val contactName = requireView().findViewById<TextView>(R.id.contactDetailsName)
                    val firstPhone = requireView().findViewById<TextView>(R.id.contactDetailsPhoneFirst)
                    val secondPhone = requireView().findViewById<TextView>(R.id.contactDetailsPhoneSecond)
                    val firstEmail = requireView().findViewById<TextView>(R.id.contactDetailsEmailFirst)
                    val secondEmail = requireView().findViewById<TextView>(R.id.contactDetailsEmailSecond)
                    val contactDescription = requireView().findViewById<TextView>(R.id.contactDetailsDescription)

                    if(contactName != null) {
                        contactPhoto.setImageDrawable(ContextCompat.getDrawable(requireContext(), contact.photoResId))
                        contactName.text = contact.contactName
                        firstPhone.text = contact.firstPhone
                        secondPhone.text = contact.secondPhone
                        firstEmail.text = contact.firstEmail
                        secondEmail.text = contact.secondEmail
                        contactDescription.text = contact.contactDescription
                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_CONTACT_ID = "id"

        fun newInstance(id: String): ContactDetailsFragment {
            return ContactDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CONTACT_ID, id)
                }
            }
        }
    }
}