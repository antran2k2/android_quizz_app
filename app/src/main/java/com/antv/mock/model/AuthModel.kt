package com.antv.mock.model
data class AuthModel (
    var email: String? = "",
    var password: String? = "",
    var confirmPassword: String? = ""
) {
    fun isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email?: "").matches()
    }
    fun isValidPassword(): Boolean {
        return (password?.length ?: 0) >= 6
    }
    fun isValidConfirmPassword(): Boolean {
        return confirmPassword == password
    }
    fun isValid(): Boolean {
        return isValidEmail() && isValidPassword() && isValidConfirmPassword()
    }
}
