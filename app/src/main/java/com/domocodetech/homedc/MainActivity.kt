package com.domocodetech.homedc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.domocodetech.homedc.login.ForgotPasswordScreen
import com.domocodetech.homedc.login.LoginScreen
import com.domocodetech.homedc.login.RegisterScreen
import com.domocodetech.homedc.ui.theme.HomeDcTheme
import com.domocodetech.homedc.utils.SharedPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account = task.result
        firebaseAuthWithGoogle(account)
    }

    private var isUserLoggedIn by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val token = SharedPreferencesManager.getAuthToken(this)
        isUserLoggedIn = token != null

        setContent {
            HomeDcTheme {
                val navController = rememberNavController()
                if (isUserLoggedIn) {
                    MainContent()
                } else {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { signIn() },
                                onGoogleLoginClick = { signIn() },
                                onRegisterClick = { navController.navigate("register") },
                                onForgotPasswordClick = { navController.navigate("forgotPassword") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(onRegisterClick = { email, password ->
                                // Handle registration logic
                                navController.popBackStack()
                            })
                        }
                        composable("forgotPassword") {
                            ForgotPasswordScreen(onResetClick = { email ->
                                // Handle password reset logic
                                navController.popBackStack()
                            })
                        }
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

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    isUserLoggedIn = true
                    account?.idToken?.let { SharedPreferencesManager.saveAuthToken(this, it) }
                } else {
                    // Sign in failed
                    // Handle error
                }
            }
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