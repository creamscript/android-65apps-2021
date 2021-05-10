package com.creamscript.bulychev.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.creamscript.bulychev.R
import com.creamscript.bulychev.data.Contact
import com.creamscript.bulychev.interfaces.ContactDetailsDeliverable
import com.creamscript.bulychev.services.ContactService
import com.creamscript.bulychev.utils.*
import com.creamscript.bulychev.data.REQUEST_CODE_READ_CONTACTS

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details), OnCheckedChangeListener
{
    private var serviceDeliverable: ContactService.IService? = null
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchAlarm: Switch? = null

    private lateinit var contactDetails: Contact

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ContactService.IService)
            serviceDeliverable = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity)
            .supportActionBar
                ?.setTitle(R.string.title_contact_details)

        switchAlarm = requireView().findViewById<Switch>(R.id.contactDetailsSwitchBirthday)
        switchAlarm?.setOnCheckedChangeListener(this)

        val hasReadContactPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            serviceDeliverable?.getService()?.getContact(callback, "2")
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        }
    }

    override fun onDestroyView() {
        switchAlarm = null
        super.onDestroyView()
    }

    override fun onDetach() {
        serviceDeliverable = null
        super.onDetach()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
         if (isChecked) {
            if(!contactDetails.isAlarmSet(requireContext())) {
                 contactDetails.scheduleNotify(requireContext(), nextBirthday(contactDetails.dateBirthday))
            }
         } else {
             if(contactDetails.isAlarmSet(requireContext())) {
                 contactDetails.stopNotify(requireContext())
             }
         }
    }

    private val callback = object : ContactDetailsDeliverable {
        @SuppressLint("SetTextI18n", "UseSwitchCompatOrMaterialCode")
        override fun getContactDetails(contact: Contact) {
            contactDetails = contact
            requireView().post {
                val contactPhoto = requireView().findViewById<ImageView>(R.id.contactDetailsPhoto)
                val contactName = requireView().findViewById<TextView>(R.id.contactDetailsName)
                val firstPhone = requireView().findViewById<TextView>(R.id.contactDetailsPhoneFirst)
                val secondPhone = requireView().findViewById<TextView>(R.id.contactDetailsPhoneSecond)
                val firstEmail = requireView().findViewById<TextView>(R.id.contactDetailsEmailFirst)
                val secondEmail = requireView().findViewById<TextView>(R.id.contactDetailsEmailSecond)
                val contactDescription = requireView().findViewById<TextView>(R.id.contactDetailsDescription)
                val birthdayDate = requireView().findViewById<TextView>(R.id.contactDetailsBirthday)
                val switchNotify = requireView().findViewById<Switch>(R.id.contactDetailsSwitchBirthday)

                if(contactName != null) {
                    contactPhoto.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            contact.photoResId
                        )
                    )
                    contactName.text = contact.contactName
                    firstPhone.text = contact.firstPhone
                    secondPhone.text = contact.secondPhone
                    firstEmail.text = contact.firstEmail
                    secondEmail.text = contact.secondEmail
                    contactDescription.text = contact.contactDescription
                    birthdayDate.text = contact.dateBirthday
                    if (contact.isAlarmSet(requireContext())) switchNotify.isChecked = true
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_CONTACTS ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    serviceDeliverable?.getService()?.getContact(callback, "2")
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.msg_deny_permission_contacts,
                        Toast.LENGTH_LONG).show()
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