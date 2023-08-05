package com.sanket.donatetoday.repository

import com.google.firebase.database.FirebaseDatabase
import io.realm.kotlin.Realm
import javax.inject.Inject

class SharedRepository @Inject constructor(val database: FirebaseDatabase, val realm: Realm) {

}