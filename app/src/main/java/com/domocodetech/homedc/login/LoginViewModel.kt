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

    fun loginWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _isUserLoggedIn.value = true
                        val token = task.result?.user?.getIdToken(false)?.result?.token
                        token?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                        Log.d("LoginViewModel", "Login with email successful")
                        onSuccess()
                    } else {
                        _isUserLoggedIn.value = false
                        Log.d("LoginViewModel", "Login with email failed: ${task.exception?.message}")
                        onFailure(task.exception?.message ?: "Invalid email or password")
                    }
                }
        }
    }

    fun registerWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result?.user?.getIdToken(false)?.result?.token
                        token?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                        Log.d("LoginViewModel", "Registration with email successful")
                        onSuccess()
                    } else {
                        Log.d("LoginViewModel", "Registration with email failed: ${task.exception?.message}")
                        onFailure(task.exception?.message ?: "Unknown error")
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

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "Password reset email sent successfully")
                    onSuccess()
                } else {
                    Log.d("LoginViewModel", "Password reset email failed: ${task.exception?.message}")
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun setUserLoggedIn(loggedIn: Boolean) {
        _isUserLoggedIn.value = loggedIn
    }
}