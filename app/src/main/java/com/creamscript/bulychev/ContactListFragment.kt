package com.creamscript.bulychev

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {

    private var contactSelectable: ContactSelectable? = null
    private var contactListLayout: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactSelectable)
            contactSelectable = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)
                ?.supportActionBar
                ?.setTitle(R.string.title_contact_list)

        contactListLayout = view.findViewById(R.id.contactListLayout)
        contactListLayout?.setOnClickListener{
            contactSelectable?.contactSelected("1")
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
}