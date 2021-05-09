package com.creamscript.bulychev.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.creamscript.bulychev.R
import com.creamscript.bulychev.fragments.ContactDetailsFragment
import com.creamscript.bulychev.fragments.ContactListFragment
import com.creamscript.bulychev.interfaces.ContactSelectable
import com.creamscript.bulychev.services.ContactService
import com.creamscript.bulychev.data.CONTACT_ID
import com.creamscript.bulychev.data.FRAGMENT_ID
import com.creamscript.bulychev.data.CONTACT_DETAILS_LAYOUT_ID

class MainActivity : AppCompatActivity(), ContactSelectable, ContactService.IService
{
    private var contactService : ContactService? = null
    private var bound = false
    private var isCreateMainFragment = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true

            navigateToFragmentByIntent()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isCreateMainFragment = savedInstanceState  == null

        val intent = Intent(this, ContactService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        if(bound) {
            unbindService(connection)
            bound = false
        }
        contactService = null
        super.onDestroy()
    }

    override fun contactSelected(id: String) {
        openContactDetails(R.id.fragment_container, ContactDetailsFragment.newInstance(id))
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
        val contactDetailsFragment = intent.getStringExtra(FRAGMENT_ID)
        val contactId = intent.getIntExtra(CONTACT_ID, 0)

        if (contactDetailsFragment == CONTACT_DETAILS_LAYOUT_ID) {
            openContactDetails(R.id.fragment_container, ContactDetailsFragment.newInstance(contactId.toString()))
        } else {
            if(isCreateMainFragment) {
                openContactList(R.id.fragment_container, ContactListFragment())
            }
        }
    }

    override fun getService() = contactService

}
