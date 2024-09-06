package com.domocodetech.homedc.login.viewmodel

import android.content.Context
import android.util.Log
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

    // Function to log in with email and password
    fun loginWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            authManager.loginWithEmail(email, password, context, {
                _isUserLoggedIn.value = true
                Log.d("LoginViewModel", "Login with email successful")
                onSuccess()
            }, { error ->
                _isUserLoggedIn.value = false
                Log.e("LoginViewModel", "Login with email failed: $error")
                onFailure(error)
            })
        }
    }

    // Function to register with email and password
    fun registerWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            authManager.registerWithEmail(email, password, context, {
                Log.d("LoginViewModel", "Registration with email successful")
                onSuccess()
            }, { error ->
                Log.e("LoginViewModel", "Registration with email failed: $error")
                onFailure(error)
            })
        }
    }

    // Function to log in with Google account
    fun loginWithGoogle(account: GoogleSignInAccount?, context: Context) {
        viewModelScope.launch {
            authManager.loginWithGoogle(account, context, {
                _isUserLoggedIn.value = true
                Log.d("LoginViewModel", "Login with Google successful")
            }, { error ->
                _isUserLoggedIn.value = false
                Log.e("LoginViewModel", "Login with Google failed: $error")
            })
        }
    }

    // Function to reset password
    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            authManager.resetPassword(email, {
                Log.d("LoginViewModel", "Password reset email sent successfully")
                onSuccess()
            }, { error ->
                Log.e("LoginViewModel", "Password reset email failed: $error")
                onFailure(error)
            })
        }
    }

    // Function to set user login status
    fun setUserLoggedIn(loggedIn: Boolean) {
        _isUserLoggedIn.value = loggedIn
    }
}