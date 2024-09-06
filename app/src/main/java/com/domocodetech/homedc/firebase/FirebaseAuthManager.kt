package com.domocodetech.homedc.firebase

import android.content.Context
import android.util.Log
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FirebaseAuthManager(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    fun loginWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.user?.getIdToken(false)?.result?.token
                    token?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                    Log.d("FirebaseAuthManager", "Login with email successful")
                    onSuccess()
                } else {
                    Log.d("FirebaseAuthManager", "Login with email failed: ${task.exception?.message}")
                    onFailure(task.exception?.message ?: "Invalid email or password")
                }
            }
    }

    fun registerWithEmail(email: String, password: String, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.user?.getIdToken(false)?.result?.token
                    token?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                    Log.d("FirebaseAuthManager", "Registration with email successful")
                    onSuccess()
                } else {
                    Log.d("FirebaseAuthManager", "Registration with email failed: ${task.exception?.message}")
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun loginWithGoogle(account: GoogleSignInAccount?, context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    account?.idToken?.let { SharedPreferencesManager.saveAuthToken(context, it) }
                    Log.d("FirebaseAuthManager", "Login with Google successful")
                    onSuccess()
                } else {
                    Log.d("FirebaseAuthManager", "Login with Google failed: ${task.exception?.message}")
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuthManager", "Password reset email sent successfully")
                    onSuccess()
                } else {
                    Log.d("FirebaseAuthManager", "Password reset email failed: ${task.exception?.message}")
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }
}