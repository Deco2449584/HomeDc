package com.domocodetech.homedc.navigation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.domocodetech.homedc.explorer.ExplorerScreen
import com.domocodetech.homedc.firebase.signIn
import com.domocodetech.homedc.home.HomeScreen
import com.domocodetech.homedc.login.presentation.ForgotPasswordScreen
import com.domocodetech.homedc.login.presentation.LoginScreen
import com.domocodetech.homedc.login.presentation.RegisterScreen
import com.domocodetech.homedc.login.viewmodel.LoginViewModel
import com.domocodetech.homedc.profile.ProfileScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    isUserLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    googleSignInClient: GoogleSignInClient,
    context: Context,
    signInLauncher: ActivityResultLauncher<Intent>
) {
    Scaffold(
        bottomBar = {
            if (isUserLoggedIn) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) Routes.HOME_ROUTE else Routes.LOGIN_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN_ROUTE) {
                LoginScreen(
                    onLoginClick = { email, password ->
                        loginViewModel.loginWithEmail(email, password, context, {
                            navController.navigate(Routes.HOME_ROUTE)
                        }, { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        })
                    },
                    onGoogleLoginClick = {
                        signIn(
                            googleSignInClient,
                            navController,
                            signInLauncher
                        )
                    },
                    onRegisterClick = { navController.navigate(Routes.REGISTER_ROUTE) },
                    onForgotPasswordClick = { navController.navigate(Routes.FORGOT_PASSWORD_ROUTE) },
                    navController = navController
                )
            }

            composable(Routes.REGISTER_ROUTE) {
                RegisterScreen(onRegisterClick = { email, password ->
                    loginViewModel.registerWithEmail(email, password, context, {
                        navController.popBackStack()
                    }, { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                }, navController = navController)
            }

            composable(Routes.FORGOT_PASSWORD_ROUTE) {
                ForgotPasswordScreen(onResetClick = { email ->
                    loginViewModel.resetPassword(email, {
                        navController.popBackStack()
                    }, { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                }, navController = navController)
            }

            composable(Routes.HOME_ROUTE) {
                HomeScreen()
            }

            composable(Routes.EXPLORER_ROUTE) {
                ExplorerScreen()
            }

            composable(Routes.PROFILE_ROUTE) {
                ProfileScreen(onLogout = {
                    navController.navigate(Routes.LOGIN_ROUTE) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }, context = context)
            }
        }
    }
}