package com.creamscript.bulychev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(), ContactSelectable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState  == null)
            openContactList(R.id.fragment_container, ContactListFragment())
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
}