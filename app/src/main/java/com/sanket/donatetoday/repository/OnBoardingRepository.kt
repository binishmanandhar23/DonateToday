package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.sanket.donatetoday.database.firebase.enums.FirebasePaths
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.User
import com.sanket.donatetoday.ui.states.LoginUIState
import javax.inject.Inject

class OnBoardingRepository @Inject constructor(private val database: DatabaseReference, private val auth: FirebaseAuth) {

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

    fun onSignUp(user: User, onState: (LoginUIState) -> Unit) {
        onState(LoginUIState.Loading)
        database.child(FirebasePaths.Emails.node).get().addOnSuccessListener { dataSnapshot ->
            (dataSnapshot.getValue<List<String>>()).let {
                if(it == null){
                    database.child(FirebasePaths.Emails.node).setValue(listOf(user.emailAddress))
                    setSignUpData(user = user, onState = onState)
                } else if(it.contains(user.emailAddress)) {
                    onState(LoginUIState.Error("Email already exists"))
                } else{
                    val newList = it.toMutableList()
                    newList.add(user.emailAddress)
                    database.child(FirebasePaths.Emails.node).setValue(newList)
                }
            }
        }.addOnFailureListener {
            setSignUpData(user = user, onState = onState)
        }
    }

    private fun setSignUpData(user: User, onState: (LoginUIState) -> Unit){
        auth.createUserWithEmailAndPassword(user.emailAddress, user.password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                database.child(FirebasePaths.Emails.node).setValue(listOf(user.emailAddress))
                database.child(FirebasePaths.Users.node).let {
                    when (user.userType) {
                        UserType.Donor.type -> it.child(FirebasePaths.Donors.node)
                        UserType.Organization.type -> it.child(FirebasePaths.Organizations.node)
                        else -> it
                    }
                }.child(user.id).setValue(user)
                onState(LoginUIState.Success("Successfully created user"))
            } else
                onState(LoginUIState.Error(task.exception?.message))
        }
    }
}