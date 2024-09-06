package com.domocodetech.homedc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.domocodetech.homedc.login.LoginViewModel
import com.domocodetech.homedc.navigation.NavGraph
import com.domocodetech.homedc.ui.theme.HomeDcTheme
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val loginViewModel: LoginViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account = task.result
        loginViewModel.loginWithGoogle(account, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val token = SharedPreferencesManager.getAuthToken(this)
        loginViewModel.setUserLoggedIn(token != null)

        setContent {
            HomeDcTheme {
                val navController = rememberNavController()
                val isUserLoggedIn by loginViewModel.isUserLoggedIn.collectAsState()

                NavGraph(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    loginViewModel = loginViewModel,
                    googleSignInClient = googleSignInClient,
                    context = this,
                    signInLauncher = signInLauncher
                )
            }
        }
    }
}