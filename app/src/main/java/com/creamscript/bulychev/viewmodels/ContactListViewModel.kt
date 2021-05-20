package com.creamscript.bulychev.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.core.Single
import com.creamscript.bulychev.ContactRepository
import com.creamscript.bulychev.models.SimpleContact

class ContactListViewModel: ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val contactRepository = ContactRepository()
    private val contactList = MutableLiveData<List<SimpleContact>>()
    private val loadingStatus = MutableLiveData<Boolean>()

    fun loadContactList(context: Context, query: String?) {
        contactRepository.getContacts(context, query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStatus.postValue(true) }
            .subscribeBy(
                onSuccess = {
                    contactList.postValue(it)
                    loadingStatus.postValue(false)
                },
                onError = {
                    loadingStatus.postValue(false)
                    Log.d("loadContactList", it.toString())
                }
            )
            .addTo(disposable)
    }

    fun getContactList(): LiveData<List<SimpleContact>> {
        return contactList
    }

    fun getLoadingStatus(): LiveData<Boolean> {
        return loadingStatus
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}