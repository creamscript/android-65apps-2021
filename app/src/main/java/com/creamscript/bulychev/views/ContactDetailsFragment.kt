package com.creamscript.bulychev.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.creamscript.bulychev.R
import com.creamscript.bulychev.REQUEST_CODE_READ_CONTACTS
import com.creamscript.bulychev._utils.nextBirthday
import com.creamscript.bulychev.models.Contact
import com.creamscript.bulychev.viewmodels.ContactDetailsViewModel

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details), OnCheckedChangeListener
{
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchAlarm: Switch? = null
    private var contactDetailsViewModel: ContactDetailsViewModel? = null

    private lateinit var contactDetails: Contact
    private val contactId: String by lazy {
        requireArguments().getString(ARG_CONTACT_ID, "1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactDetailsViewModel = ViewModelProvider(this).get(ContactDetailsViewModel::class.java)
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
            contactDetailsViewModel?.loadContact(requireContext(), contactId)
            contactDetailsViewModel
                ?.getContact()
                ?.observe(viewLifecycleOwner, { postContactToDetails(it) })
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_CONTACTS ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactDetailsViewModel?.loadContact(requireContext(), contactId)
                    contactDetailsViewModel
                        ?.getContact()
                        ?.observe(viewLifecycleOwner, { postContactToDetails(it) })
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.msg_deny_permission_contacts,
                        Toast.LENGTH_LONG).show()
                }
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun postContactToDetails (contact: Contact) {
        contactDetails = contact
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
            contactPhoto.setImageResource(contact.photoResId)
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