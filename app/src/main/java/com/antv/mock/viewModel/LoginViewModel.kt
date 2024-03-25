package com.antv.mock.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.antv.mock.MainActivity
import com.antv.mock.R
import com.antv.mock.SignUpActivity
import com.antv.mock.TAG
import com.antv.mock.model.LoginModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel(
    val context: Activity
): ViewModel() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    val email: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()

    val message: ObservableField<String> = ObservableField()
    val isFailure: ObservableField<Boolean> = ObservableField()


    fun handleLogin() {
        val signIn = LoginModel(email.get(), password.get())
        if (!signIn.isValidEmail()) {
            isFailure.set(true)
            message.set(context.getString(R.string.invalid_username))
            return
        }
        if (!signIn.isValidPassword()) {
            isFailure.set(true)
            message.set(context.getString(R.string.invalid_password))
            return
        }
        login(signIn.email!!, signIn.password!!)

    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveCredentials(email, password)
                    navigateToMain()
                    context.finish()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    isFailure.set(true)
                    message.set(context.getString(R.string.login_failed))
                }
            }
    }

    private fun saveCredentials(email: String, password: String) {
        val sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        // You might not want to save the password in plaintext for security reasons.
        // Instead, consider using a more secure method like encrypting the password.
//        editor.putString("password", password)
        editor.apply()
    }

    fun navigateToSignUp() {
        Intent(context, SignUpActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    fun navigateToMain() {
        Intent(context, MainActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    fun checkCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signInWithGoogle() {
        oneTapClient = Identity.getSignInClient(context)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(context) { result ->
                try {
                    context.startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                        )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(context) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(context) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user = auth.currentUser
                                        updateUI(user)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                                        updateUI(null)
                                    }
                                }
                        }
                        password != null -> {

                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }


    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            navigateToMain()
            context.finish()
        } else {
            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }


    fun onForgotPasswordClicked() {
        Toast.makeText(context, "This feature is under development", Toast.LENGTH_SHORT).show()
    }
}