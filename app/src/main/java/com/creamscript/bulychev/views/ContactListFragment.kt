package com.creamscript.bulychev.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creamscript.bulychev.R
import com.creamscript.bulychev.REQUEST_CODE_READ_CONTACTS
import com.creamscript.bulychev.viewmodels.ContactListViewModel

class ContactListFragment : Fragment(R.layout.fragment_contact_list),
    SearchView.OnQueryTextListener {

    private var contactListViewModel: ContactListViewModel? = null
    private var contactSelectable: ContactSelectable? = null
    private var contactListRecyclerView: RecyclerView? = null
    private var contactsAdapter: ContactsAdapter? = null
    private var progressBarForContactList: ProgressBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactSelectable)
            contactSelectable = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        contactListViewModel = ViewModelProvider(this).get(ContactListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity)
                .supportActionBar
                ?.setTitle(R.string.title_contact_list)

        progressBarForContactList = view.findViewById(R.id.progressBarForContactList)

        contactsAdapter = ContactsAdapter { simpleContact -> contactSelectable?.contactSelected(simpleContact) }
        contactListRecyclerView = view.findViewById(R.id.recyclerViewForContactList)
        contactListRecyclerView?.addItemDecoration(SimpleOffsetItemDecoration(10))
        contactListRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        contactListRecyclerView?.adapter = contactsAdapter

        contactListViewModel
            ?.getLoadingStatus()
            ?.observe(viewLifecycleOwner, {
                    it?.let {
                        when(it) {
                            true -> progressBarForContactList?.visibility = View.VISIBLE
                            false -> progressBarForContactList?.visibility = View.GONE
                        }
                    }
            })

        val hasReadContactPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            contactListViewModel?.loadContactList(requireContext(), null)
            contactListViewModel
                ?.getContactList()
                ?.observe(viewLifecycleOwner, {
                    it?.let {
                        contactsAdapter?.submitList(it)
                    }
                })

        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_READ_CONTACTS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_search, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchContacts(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchContacts(query)
        }
        return true
    }

    private fun searchContacts(query: String) {
        contactListViewModel?.loadContactList(requireContext(), query)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_CONTACTS ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactListViewModel?.loadContactList(requireContext(), null)
                    contactListViewModel
                        ?.getContactList()
                        ?.observe(viewLifecycleOwner, {
                            it?.let {
                                contactsAdapter?.submitList(it)
                            }
                        })

                } else {
                    Toast.makeText(
                            requireContext(),
                            R.string.msg_deny_permission_contacts,
                            Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroyView() {
        progressBarForContactList = null
        contactListRecyclerView?.adapter = null
        contactListRecyclerView?.layoutManager = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactSelectable = null
        super.onDetach()
    }
}