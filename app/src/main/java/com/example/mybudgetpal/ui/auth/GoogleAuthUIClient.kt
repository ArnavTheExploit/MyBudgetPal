package com.example.mybudgetpal.ui.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GoogleAuthUIClient(private val context: Context) {

    private val auth = Firebase.auth
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("884134855943-3t9fk8tn62rp40v4lctlt2st4vsv1phd.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    fun signInWithGoogle(
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                launcher.launch(intentSenderRequest)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun handleSignInResult(data: Intent?, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it) }
            } else {
                onError(Exception("Google ID Token is null"))
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
}
