package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import com.sanket.donatetoday.ui.states.LoginUIState
import javax.inject.Inject

class OnBoardingRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun isUserLoggedIn() = auth.currentUser != null


    fun onSignIn(email: String, password: String, onState: (LoginUIState) -> Unit) {
        onState(LoginUIState.Loading)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onState(LoginUIState.Success("Welcome back!"))
            } else
                onState(LoginUIState.Error(task.exception?.message))
        }
    }

    fun onSignUp(email: String, password: String, onState: (LoginUIState) -> Unit) {
        onState(LoginUIState.Loading)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onState(LoginUIState.Success("Successfully created user"))
            } else
                onState(LoginUIState.Error(task.exception?.message))
        }
    }
}