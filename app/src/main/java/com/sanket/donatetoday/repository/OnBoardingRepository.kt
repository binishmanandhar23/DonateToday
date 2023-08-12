package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.sanket.donatetoday.database.firebase.enums.FirebasePaths
import com.sanket.donatetoday.models.dto.EmailDTO
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.toUserDTO
import com.sanket.donatetoday.models.dto.toUserEntity
import com.sanket.donatetoday.models.entity.UserEntity
import com.sanket.donatetoday.ui.states.LoginUIState
import com.sanket.donatetoday.utils.DatabaseUtils.addDonationItems
import com.sanket.donatetoday.utils.DatabaseUtils.addListOfEmails
import com.sanket.donatetoday.utils.DatabaseUtils.addUser
import com.sanket.donatetoday.utils.DatabaseUtils.getUser
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import javax.inject.Inject

class OnBoardingRepository @Inject constructor(private val database: DatabaseReference, private val auth: FirebaseAuth, private val realm: Realm) {

    fun isUserLoggedIn() = auth.currentUser != null


    fun syncCurrentUserDataToDatabase(email: String? = auth.currentUser?.email, onSuccess: (UserDTO) -> Unit, onError: (String?) -> Unit){
        getUserFromRealm(email = email)?.let {
            database.addUser(it.toUserDTO(), onSuccess = {
                onSuccess(it.toUserDTO())
            }, onError = {
                onSuccess(it.toUserDTO())
            })
        }
    }

    private fun getUser(email: String? = auth.currentUser?.email, onSuccess: (UserDTO) -> Unit, onError: (String?) -> Unit) {
        if(email == null){
            onError("User not found")
            return
        }
        database.getUser(email = email, onSuccess = {
            saveUserToRealm(userEntity = it.toUserEntity())
            onSuccess(it)
        }, onError = onError)
    }


    fun onSignIn(email: String, password: String, onSuccess: (UserDTO) -> Unit, onError: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getUser(email = email, onSuccess = onSuccess, onError = onError)
            } else
                onError(task.exception?.message)
        }
    }

    fun onSignUp(userDTO: UserDTO, onState: (LoginUIState) -> Unit) {
        onState(LoginUIState.Loading)
        database.child(FirebasePaths.Emails.node).get().addOnSuccessListener { dataSnapshot ->
            (dataSnapshot.getValue<List<EmailDTO>>()).let { listOfEmailDTO ->
                if(listOfEmailDTO == null){
                    setSignUpData(userDTO = userDTO, onState = onState, listOfEmails = listOf(EmailDTO(email = userDTO.emailAddress, id = userDTO.id, userType = userDTO.userType)))
                } else if(listOfEmailDTO.map { it.email }.contains(userDTO.emailAddress)) {
                    onState(LoginUIState.Error("Email already exists"))
                } else{
                    val newList = listOfEmailDTO.toMutableList()
                    newList.add(EmailDTO(email = userDTO.emailAddress, id = userDTO.id, userType = userDTO.userType))
                    setSignUpData(userDTO = userDTO, onState = onState, listOfEmails = newList)
                }
            }
        }.addOnFailureListener {
            setSignUpData(userDTO = userDTO, onState = onState, listOfEmails = listOf(EmailDTO(email = userDTO.emailAddress, id = userDTO.id, userType = userDTO.userType)))
        }
    }

    private fun setSignUpData(userDTO: UserDTO,listOfEmails: List<EmailDTO>, onState: (LoginUIState) -> Unit){
        auth.createUserWithEmailAndPassword(userDTO.emailAddress, userDTO.password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                database.addListOfEmails(listOfEmailDTO = listOfEmails)
                database.addDonationItems(userDTO = userDTO)
                database.addUser(userDTO = userDTO)
                saveUserToRealm(userEntity = userDTO.toUserEntity())
                onState(LoginUIState.Success("Successfully created user"))
            } else
                onState(LoginUIState.Error(task.exception?.message))
        }
    }

    private fun saveUserToRealm(userEntity: UserEntity) =
        realm.writeBlocking {
            copyToRealm(userEntity, updatePolicy = UpdatePolicy.ALL)
        }

    private fun getUserFromRealm(email: String?) = realm.query<UserEntity>("emailAddress == $0", email).first().find()
}