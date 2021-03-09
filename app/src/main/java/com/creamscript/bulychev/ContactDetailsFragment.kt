package com.creamscript.bulychev

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)
                ?.supportActionBar
                ?.setTitle(R.string.title_contact_details)
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