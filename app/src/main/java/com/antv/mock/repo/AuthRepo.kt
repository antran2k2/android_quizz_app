package com.antv.mock.repo

import com.antv.mock.model.Response
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    fun loginWithEmail(
        email: String,
        password: String,
        callback: (Response<FirebaseUser>) -> Unit
    )

    fun loginWithCredential(
        firebaseCredential: AuthCredential,
        callback: (Response<FirebaseUser>) -> Unit
    )

    fun signUp(
        email: String,
        password: String,
        callback: (Response<FirebaseUser>) -> Unit
    )

    fun getCurrentUser(): Response<FirebaseUser>

    fun logout()
}