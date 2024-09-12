package com.domocodetech.homedc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.domocodetech.homedc.login.viewmodel.LoginViewModel
import com.domocodetech.homedc.navigation.NavGraph
import com.domocodetech.homedc.ui.theme.HomeDcTheme
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Declare a lateinit variable for GoogleSignInClient
    private lateinit var googleSignInClient: GoogleSignInClient
    // Declare a LoginViewModel using the viewModels delegate
    private val loginViewModel: LoginViewModel by viewModels()

    // Register an ActivityResultLauncher for Google Sign-In
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result of the sign-in intent
        handleSignInResult(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the GoogleSignInClient
        initializeGoogleSignInClient()
        // Check the user's login status
        checkUserLoginStatus()
        // Set the content view using Compose
        setContent {
            // Setup the UI content
            HomeDcTheme {
                // Create a NavController
                val navController = rememberNavController()
                // Collect the user login state from LoginViewModel
                val isUserLoggedIn by loginViewModel.isUserLoggedIn.collectAsState()
                // Define the updateLoginState function
                val updateLoginState: (Boolean) -> Unit = { isLoggedIn ->
                    loginViewModel.setUserLoggedIn(isLoggedIn)
                }
                // Set up the navigation graph
                NavGraph(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    loginViewModel = loginViewModel,
                    googleSignInClient = googleSignInClient,
                    context = this,
                    signInLauncher = signInLauncher,
                    updateLoginState = updateLoginState

                )
            }
        }
    }

    // Initialize the GoogleSignInClient with the required options
    private fun initializeGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    // Check if the user is logged in by retrieving the auth token from SharedPreferences
    private fun checkUserLoginStatus() {
        val token = SharedPreferencesManager.getAuthToken(this)
        loginViewModel.setUserLoggedIn(token != null)
    }

    // Handle the result of the Google sign-in intent
    private fun handleSignInResult(data: Intent?) {
        try {
            // Get the GoogleSignInAccount from the intent data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            // Call loginWithGoogle method in LoginViewModel
            loginViewModel.loginWithGoogle(account, this)
        } catch (e: ApiException) {
            // Log the error if sign-in fails
            Log.e("MainActivity", "Google sign-in failed", e)
        }
    }

}