package com.domocodetech.homedc.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import com.domocodetech.homedc.R
import com.domocodetech.homedc.login.viewmodel.LoginViewModel
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInHelper(private val context: Context) {

    lateinit var googleSignInClient: GoogleSignInClient

    fun initializeGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun checkUserLoginStatus(loginViewModel: LoginViewModel) {
        val token = SharedPreferencesManager.getAuthToken(context)
        loginViewModel.setUserLoggedIn(token != null)
    }

    fun handleSignInResult(data: Intent?, loginViewModel: LoginViewModel) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            loginViewModel.loginWithGoogle(account, context)
        } catch (e: ApiException) {
            Log.e("GoogleSignInHelper", "Google sign-in failed", e)
        }
    }
}