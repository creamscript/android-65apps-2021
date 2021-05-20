package com.creamscript.bulychev.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import com.creamscript.bulychev.ContactRepository
import com.creamscript.bulychev.models.Contact

class ContactDetailsViewModel : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val contactRepository = ContactRepository()
    private val contact = MutableLiveData<Contact>()
    private val loadingStatus = MutableLiveData<Boolean>()

    fun loadContact(context: Context, id: String) {
        contactRepository.getContact(context, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStatus.postValue(true) }
            .subscribeBy(
                onSuccess = {
                    contact.postValue(it)
                    loadingStatus.postValue(false)
                },
                onError = {
                    loadingStatus.postValue(false)
                    Log.d("loadContact", it.toString())
                }
            )
            .addTo(disposable)
    }

    fun getContact() : LiveData<Contact>  {
        return contact
    }

    fun getLoadingStatus(): LiveData<Boolean> {
        return loadingStatus
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}