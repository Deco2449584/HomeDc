package com.domocodetech.homedc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.domocodetech.homedc.firebase.GoogleSignInHelper
import com.domocodetech.homedc.login.viewmodel.LoginViewModel
import com.domocodetech.homedc.navigation.NavGraph
import com.domocodetech.homedc.ui.theme.HomeDcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleSignInHelper: GoogleSignInHelper
    private val loginViewModel: LoginViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        googleSignInHelper.handleSignInResult(result.data, loginViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleSignInHelper = GoogleSignInHelper(this)
        googleSignInHelper.initializeGoogleSignInClient()
        googleSignInHelper.checkUserLoginStatus(loginViewModel)
        setContent {
            SetupUI()
        }
    }

    @Composable
    private fun SetupUI() {
        HomeDcTheme {
            val navController = rememberNavController()
            val isUserLoggedIn by loginViewModel.isUserLoggedIn.collectAsState()
            NavGraph(
                navController = navController,
                isUserLoggedIn = isUserLoggedIn,
                loginViewModel = loginViewModel,
                googleSignInClient = googleSignInHelper.googleSignInClient,
                context = this,
                signInLauncher = signInLauncher
            )
        }
    }
}