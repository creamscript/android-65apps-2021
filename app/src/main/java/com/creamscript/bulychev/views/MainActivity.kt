package com.creamscript.bulychev.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.creamscript.bulychev.*
import com.creamscript.bulychev.models.SimpleContact

class MainActivity : AppCompatActivity(), ContactSelectable
{
    private var isCreateMainFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isCreateMainFragment = savedInstanceState  == null

        navigateToFragmentByIntent()
    }

    override fun contactSelected(simpleContact: SimpleContact) {
        openContactDetails(R.id.fragment_container, ContactDetailsFragment.newInstance(simpleContact.contactId))
    }

    private fun openContactList(containerViewId: Int, fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(containerViewId, fragment)
                .commit()
    }

    private fun openContactDetails(containerViewId: Int, fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun navigateToFragmentByIntent() {
        val fragmentIdFromIntent = intent.getStringExtra(FRAGMENT_ID)
        val contactIdFromIntent = intent.getIntExtra(CONTACT_ID, 0)

        if (fragmentIdFromIntent == CONTACT_DETAILS_FRAG_ID) {
            openContactDetails(R.id.fragment_container, ContactDetailsFragment.newInstance(contactIdFromIntent.toString()))
        } else {
            if(isCreateMainFragment) {
                openContactList(R.id.fragment_container, ContactListFragment())
            }
        }
    }

}
