package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class OnBoardingRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun isUserLoggedIn() = auth.currentUser != null
}