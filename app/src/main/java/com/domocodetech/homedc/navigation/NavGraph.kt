package com.domocodetech.homedc.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.domocodetech.homedc.home.HomeScreen
import com.domocodetech.homedc.login.ForgotPasswordScreen
import com.domocodetech.homedc.login.LoginScreen
import com.domocodetech.homedc.login.LoginViewModel
import com.domocodetech.homedc.login.RegisterScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun NavGraph(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    googleSignInClient: GoogleSignInClient,
    context: Context,
    signInLauncher: ActivityResultLauncher<Intent>
) {
    NavHost(navController = navController, startDestination = if (isUserLoggedIn) "main" else "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = { email, password ->
                    loginViewModel.loginWithEmail(email, password, context, {
                        navController.navigate("main")
                    }, { error ->
                        // Handle login failure (e.g., show a toast or dialog)
                    })
                },
                onGoogleLoginClick = { signIn(googleSignInClient, navController, signInLauncher) },
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgotPassword") },
                navController = navController
            )
        }
        composable("register") {
            RegisterScreen(onRegisterClick = { email, password ->
                loginViewModel.registerWithEmail(email, password, context, {
                    navController.popBackStack()
                }, { errorMessage ->
                    // Handle registration failure (e.g., show a toast or dialog)
                })
            }, navController = navController)
        }
        composable("forgotPassword") {
            ForgotPasswordScreen(onResetClick = { email ->
                loginViewModel.resetPassword(email, {
                    // Handle success (e.g., show a toast or dialog)
                    navController.popBackStack()
                }, { errorMessage ->
                    // Handle failure (e.g., show a toast or dialog)
                })
            }, navController = navController)
        }
        composable("main") {
            HomeScreen()
        }
    }
}

private fun signIn(googleSignInClient: GoogleSignInClient, navController: NavHostController, signInLauncher: ActivityResultLauncher<Intent>) {
    val signInIntent = googleSignInClient.signInIntent
    signInLauncher.launch(signInIntent)
}