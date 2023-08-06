package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanket.donatetoday.models.entity.UserEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import javax.inject.Inject

class SharedRepository @Inject constructor(val database: FirebaseDatabase,val auth: FirebaseAuth, val realm: Realm) {

    fun getUserFromRealm(email: String? = auth.currentUser?.email) = realm.query<UserEntity>("emailAddress == $0", email).find().asFlow()

    suspend fun saveUserToRealm(userEntity: UserEntity) =
        realm.write {
            copyToRealm(userEntity, updatePolicy = UpdatePolicy.ALL)
        }
}