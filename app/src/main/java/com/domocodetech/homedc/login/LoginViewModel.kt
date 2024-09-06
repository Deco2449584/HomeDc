package com.domocodetech.homedc.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun loginWithEmail(email: String, password: String, context: Context) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isUserLoggedIn.value = true
                        val token = task.result?.user?.getIdToken(false)?.result?.token
                        token?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                        Log.d("LoginViewModel", "Login with email successful")
                    } else {
                        _isUserLoggedIn.value = false
                        Log.d("LoginViewModel", "Login with email failed: ${task.exception?.message}")
                    }
                }
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount?, context: Context) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isUserLoggedIn.value = true
                    account?.idToken?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                    Log.d("LoginViewModel", "Login with Google successful")
                } else {
                    _isUserLoggedIn.value = false
                    Log.d("LoginViewModel", "Login with Google failed: ${task.exception?.message}")
                }
            }
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        _isUserLoggedIn.value = loggedIn
    }
}