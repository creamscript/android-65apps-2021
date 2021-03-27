package com.creamscript.bulychev

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), ContactSelectable, ContactService.IService
{
    private var contactService : ContactService? = null
    private var bound = false
    private var isCreateMainFragment = false

    private val connection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true

            if(isCreateMainFragment) {
                openContactList(R.id.fragment_container, ContactListFragment())
            }
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

    override fun getService() = contactService
}