// HomeViewModel.kt
package com.domocodetech.homedc.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeViewModel : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        _user.value = currentUser
    }
}