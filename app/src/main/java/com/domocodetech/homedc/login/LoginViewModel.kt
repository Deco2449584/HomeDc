package com.domocodetech.homedc.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domocodetech.homedc.firebase.FirebaseAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val authManager = FirebaseAuthManager()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun loginWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            authManager.loginWithEmail(email, password, context, {
                _isUserLoggedIn.value = true
                onSuccess()
            }, { error ->
                _isUserLoggedIn.value = false
                onFailure(error)
            })
        }
    }

    fun registerWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            authManager.registerWithEmail(email, password, context, onSuccess, onFailure)
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount?, context: Context) {
        viewModelScope.launch {
            authManager.loginWithGoogle(account, context, {
                _isUserLoggedIn.value = true
            }, {
                _isUserLoggedIn.value = false
            })
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            authManager.resetPassword(email, onSuccess, onFailure)
        }
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        _isUserLoggedIn.value = loggedIn
    }
}