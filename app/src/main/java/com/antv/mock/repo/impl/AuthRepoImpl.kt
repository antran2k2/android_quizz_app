package com.antv.mock.repo.impl

import com.antv.mock.model.Response
import com.antv.mock.repo.AuthRepo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl : AuthRepo {

    val auth = FirebaseAuth.getInstance()
    override fun loginWithEmail(
        email: String,
        password: String,
        callback: (Response<FirebaseUser>) -> Unit
    ) {
        var user: FirebaseUser? = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    user = auth.currentUser
                    callback(Response.Success(user))
                } else {
                    callback(Response.Fail("Error logging in"))
                }
            }
            .addOnFailureListener {

                callback(Response.Fail("Error logging in"))
            }

    }

    override fun loginWithCredential(
        firebaseCredential: AuthCredential,
        callback: (Response<FirebaseUser>) -> Unit
    ) {
        var user: FirebaseUser? = null
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    callback(Response.Success(user))
                } else {
                    callback(Response.Fail("Error logging in"))
                }
            }
            .addOnFailureListener {

        callback(Response.Fail("Error logging in"))
            }
    }

    override fun signUp(
        email: String,
        password: String,
        callback: (Response<FirebaseUser>) -> Unit
    ) {
        var user: FirebaseUser? = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    callback(Response.Success(user))
                } else {
                    callback(Response.Fail("Error signing up"))
                }
            }

            .addOnFailureListener {
                callback(Response.Fail("Error signing up"))
            }

    }

    override fun getCurrentUser(): Response<FirebaseUser> {
        val user = auth.currentUser
        if (user != null) {
            return Response.Success(user)
        }
        return Response.Fail("No user logged in")
    }

    override fun logout() {
        auth.signOut()
    }
}