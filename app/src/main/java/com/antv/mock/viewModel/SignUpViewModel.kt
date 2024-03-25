package com.antv.mock.viewModel

import android.app.Activity
import androidx.databinding.ObservableField
import com.antv.mock.model.SignUpModel
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel( val context: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    val email: ObservableField<String> = ObservableField()
    val password: ObservableField<String> = ObservableField()
    val confirmPassword: ObservableField<String> = ObservableField()

    val message: ObservableField<String> = ObservableField()
    val isFailure: ObservableField<Boolean> = ObservableField()

    fun handleSignUp() {
        val signUp = SignUpModel(email.get(), password.get(), confirmPassword.get())
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
        signUp(signUp.email!!, signUp.password!!)
    }
    private fun signUp(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    message.set("Sign up successful")
                    context.finish()
                } else {
                    isFailure.set(true)
                    message.set("Sign up failed")
                }
            }
    }
}