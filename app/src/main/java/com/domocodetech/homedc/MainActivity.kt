package com.domocodetech.homedc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.domocodetech.homedc.login.ForgotPasswordScreen
import com.domocodetech.homedc.login.LoginScreen
import com.domocodetech.homedc.login.LoginViewModel
import com.domocodetech.homedc.login.RegisterScreen
import com.domocodetech.homedc.ui.theme.HomeDcTheme
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val loginViewModel: LoginViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account = task.result
        loginViewModel.loginWithGoogle(account, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val token = SharedPreferencesManager.getAuthToken(this)
        loginViewModel.setUserLoggedIn(token != null)

        setContent {
            HomeDcTheme {
                val navController = rememberNavController()
                val isUserLoggedIn by loginViewModel.isUserLoggedIn.collectAsState()

                Log.d("MainActivity", "isUserLoggedIn: $isUserLoggedIn")

                NavHost(navController = navController, startDestination = if (isUserLoggedIn) "main" else "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginClick = { email, password -> loginViewModel.loginWithEmail(email, password, this@MainActivity) },
                            onGoogleLoginClick = { signIn() },
                            onRegisterClick = { navController.navigate("register") },
                            onForgotPasswordClick = { navController.navigate("forgotPassword") },
                            navController = navController
                        )
                    }
                    composable("register") {
                        RegisterScreen(onRegisterClick = { email, password ->
                            loginViewModel.registerWithEmail(email, password, this@MainActivity, {
                                navController.popBackStack()
                            }, { errorMessage ->
                                // Handle registration failure (e.g., show a toast or dialog)
                            })
                        }, navController = navController)
                        // Handle registration logic
                    }
                    composable("forgotPassword") {
                        ForgotPasswordScreen(onResetClick = { email ->
                            // Handle password reset logic
                            navController.popBackStack()
                        })
                    }
                    composable("main") {
                        MainContent()
                    }
                }
            }
        }

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    @Composable
    fun MainContent() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Column(modifier = modifier) {
            Text(text = "Hello $name!")
        }
    }
}