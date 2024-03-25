package com.antv.mock.model

import android.util.Patterns

data class LoginModel(
    val email: String?,
    val password: String?
) {
    fun isValidEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email ?: "").matches()
    }
    fun isValidPassword(): Boolean {
        return (password?.length ?: 0) >= 6
    }
    fun isValid(): Boolean {
        return isValidEmail() && isValidPassword()
    }
}
