package com.domocodetech.homedc.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userName = MutableStateFlow(auth.currentUser?.displayName ?: "Unknown User")
    val userName: StateFlow<String> = _userName

    private val _userPhotoUrl = MutableStateFlow(auth.currentUser?.photoUrl?.toString())
    val userPhotoUrl: StateFlow<String?> = _userPhotoUrl


    fun logout(onLogout: () -> Unit, context: Context) {
        viewModelScope.launch {
            auth.signOut()
            SharedPreferencesManager.clearAuthToken(context)
            onLogout()
        }
    }
}