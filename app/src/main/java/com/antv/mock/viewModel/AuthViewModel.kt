package com.antv.mock.viewModel

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.antv.mock.R
import com.antv.mock.model.AuthModel
import com.antv.mock.model.Response
import com.antv.mock.repo.AuthRepo
import com.antv.mock.repo.impl.AuthRepoImpl
import com.antv.mock.ui.home.TAG
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class AuthViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(context) as T
    }

}

class AuthViewModel(
    private var context: Context
) : ViewModel() {

    init {
        Log.d(TAG, "init viewmodel")
    }

    private val authRepo: AuthRepo = AuthRepoImpl()
    lateinit var navController: NavController

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity

    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()


    val email: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()
    val confirmPassword: ObservableField<String> = ObservableField()


    val message: ObservableField<String> = ObservableField()
    val isFailure: ObservableField<Boolean> = ObservableField()


    fun handleLogin() {
        val signIn = AuthModel(email.get(), password.get())
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
        authRepo.loginWithEmail(signIn.email!!, signIn.password!!) { response ->
            if (response is Response.Success) {
                saveCredentials(signIn.email!!, signIn.password!!)
                userLiveData.postValue(response.data)

            } else {
                isFailure.set(true)
                Log.d(TAG, response.message ?: "Error logging in")
                message.set(context.getString(R.string.login_failed))
            }
        }

    }


    private fun saveCredentials(email: String, password: String) {
        val sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }



    fun checkCurrentUser(): FirebaseUser? {
            userLiveData.value = authRepo.getCurrentUser().data
        return authRepo.getCurrentUser().data
    }

    fun signInWithGoogle(fragment: Fragment) {
        oneTapClient = Identity.getSignInClient(context)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    fragment.startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, e.localizedMessage)
            }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            authRepo.loginWithCredential(firebaseCredential) { response ->
                                if (response is Response.Success) {
                                    userLiveData.postValue(response.data)

                                } else {

                                    isFailure.set(true)
                                    message.set(context.getString(R.string.login_failed))
                                }
                            }
                        }
                    }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }


    }



    fun onForgotPasswordClicked() {
        Toast.makeText(context, "This feature is under development", Toast.LENGTH_SHORT).show()
    }

    fun handleSignUp() {
        val signUp = AuthModel(email.get(), password.get(), confirmPassword.get())
        if (!signUp.isValidEmail()) {
            isFailure.set(true)
            message.set("Invalid email")
            return
        }
        if (!signUp.isValidPassword()) {
            isFailure.set(true)
            message.set("Invalid password")
            return
        }
        if (!signUp.isValidConfirmPassword()) {
            isFailure.set(true)
            message.set("Password does not match")
            return
        }
        authRepo.signUp(signUp.email!!, signUp.password!!) { response ->
            if (response is Response.Success) {

                userLiveData.postValue(response.data)
            } else {
                isFailure.set(true)
                message.set("Sign up failed")
            }
        }
    }

    fun logout() {
        authRepo.logout()

    }

}