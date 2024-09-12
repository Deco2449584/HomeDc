package com.domocodetech.homedc.firebase

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInClient

// Function to handle Google Sign-In
 fun signIn(
    googleSignInClient: GoogleSignInClient,
    navController: NavHostController,
    signInLauncher: ActivityResultLauncher<Intent>
) {
    val signInIntent = googleSignInClient.signInIntent
    signInLauncher.launch(signInIntent)
}