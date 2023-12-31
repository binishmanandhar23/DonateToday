package com.sanket.donatetoday.database.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = Firebase.database

    @Provides
    @Singleton
    fun provideFirebaseDatabaseReference() = Firebase.database.reference

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth
}