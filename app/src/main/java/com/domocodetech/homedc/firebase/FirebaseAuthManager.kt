package com.domocodetech.homedc.firebase

import android.content.Context
import android.util.Log
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthManager(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    // Function to log in with email and password
    fun loginWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.getIdToken(false)?.result?.token?.let { token ->
                        SharedPreferencesManager.saveAuthToken(context, token)
                    }
                    Log.d("FirebaseAuthManager", "Login with email successful")
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Invalid email or password"
                    Log.d("FirebaseAuthManager", "Login with email failed: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }

    // Function to register with email and password
    fun registerWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.getIdToken(false)?.result?.token?.let { token ->
                        SharedPreferencesManager.saveAuthToken(context, token)
                    }
                    Log.d("FirebaseAuthManager", "Registration with email successful")
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Log.d("FirebaseAuthManager", "Registration with email failed: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }

    // Function to log in with Google account
    fun loginWithGoogle(account: GoogleSignInAccount?, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    account?.idToken?.let { token ->
                        SharedPreferencesManager.saveAuthToken(context, token)
                    }
                    Log.d("FirebaseAuthManager", "Login with Google successful")
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Log.d("FirebaseAuthManager", "Login with Google failed: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }

    // Function to reset password
    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuthManager", "Password reset email sent successfully")
                    onSuccess()
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Log.d("FirebaseAuthManager", "Password reset email failed: $errorMessage")
                    onFailure(errorMessage)
                }
            }
    }
}

