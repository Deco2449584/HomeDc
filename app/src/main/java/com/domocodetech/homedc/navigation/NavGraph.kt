package com.domocodetech.homedc.navigation

import HomeScreen
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.domocodetech.homedc.login.presentation.screens.ForgotPasswordScreen
import com.domocodetech.homedc.getstarted.GetStartedScreen
import com.domocodetech.homedc.login.presentation.screens.LoginScreen
import com.domocodetech.homedc.login.viewmodel.LoginViewModel
import com.domocodetech.homedc.login.presentation.screens.RegisterScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient

// Define route names as constants
private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val FORGOT_PASSWORD_ROUTE = "forgotPassword"
private const val MAIN_ROUTE = "main"
private const val GET_STARTED_ROUTE = "getStarted"

@Composable
fun NavGraph(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    googleSignInClient: GoogleSignInClient,
    context: Context,
    signInLauncher: ActivityResultLauncher<Intent>
) {
    NavHost(navController = navController, startDestination = GET_STARTED_ROUTE) {
        composable(GET_STARTED_ROUTE) {
            GetStartedScreen(navController = navController)
        }
        // Login screen composable
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onLoginClick = { email, password ->
                    loginViewModel.loginWithEmail(email, password, context, {
                        navController.navigate(MAIN_ROUTE)
                    }, { error ->
                        // Handle login failure (e.g., show a toast or dialog)
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    })
                },
                onGoogleLoginClick = { signIn(googleSignInClient, navController, signInLauncher) },
                onRegisterClick = { navController.navigate(REGISTER_ROUTE) },
                onForgotPasswordClick = { navController.navigate(FORGOT_PASSWORD_ROUTE) },
                navController = navController
            )
        }

        // Register screen composable
        composable(REGISTER_ROUTE) {
            RegisterScreen(onRegisterClick = { email, password ->
                loginViewModel.registerWithEmail(email, password, context, {
                    navController.popBackStack()
                }, { errorMessage ->
                    // Handle registration failure (e.g., show a toast or dialog)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            }, navController = navController)
        }

        // Forgot password screen composable
        composable(FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordScreen(onResetClick = { email ->
                loginViewModel.resetPassword(email, {
                    // Handle success (e.g., show a toast or dialog)
                    navController.popBackStack()
                }, { errorMessage ->
                    // Handle failure (e.g., show a toast or dialog)
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            }, navController = navController)
        }

        // Main screen composable
        composable(MAIN_ROUTE) {
            HomeScreen()
        }
    }
}

// Function to handle Google Sign-In
private fun signIn(googleSignInClient: GoogleSignInClient, navController: NavHostController, signInLauncher: ActivityResultLauncher<Intent>) {
    val signInIntent = googleSignInClient.signInIntent
    signInLauncher.launch(signInIntent)
}