package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.sanket.donatetoday.database.firebase.enums.FirebasePaths
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.entity.UserEntity
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class SharedRepository @Inject constructor(
    val database: DatabaseReference,
    private val auth: FirebaseAuth,
    val realm: Realm
) {

    fun getUserFromRealm(email: String? = auth.currentUser?.email) =
        realm.query<UserEntity>("emailAddress == $0", email).find().asFlow()

    suspend fun saveUserToRealm(userEntity: UserEntity) =
        realm.write {
            copyToRealm(userEntity, updatePolicy = UpdatePolicy.ALL)
        }

    suspend fun getDonationItemUserModelFromType(type: String, userType: String) =
        suspendCancellableCoroutine { cont ->
            database.child(FirebasePaths.DonationItems.node).child(type).child(userType)
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    (dataSnapshot.getValue<List<DonationItemUserModel>>()).let { list ->
                        cont.resume(list) {
                            Exception(it)
                        }
                    }
                }.addOnFailureListener {
                    cont.resume(emptyList<DonationItemUserModel>()) { _ ->
                        it
                    }
                }
        }

    suspend fun getRecommendedListOfOrganizations(userDTO: UserDTO): List<DonationItemUserModel> {
        val list = mutableListOf<DonationItemUserModel>()
        userDTO.donationItemTypes.forEach { type ->
            getDonationItemUserModelFromType(
                type = type,
                userType = UserType.Organization.type
            )?.forEach { donationItemUserModel ->
                if (!list.contains(donationItemUserModel))
                    list.add(donationItemUserModel)
            }
        }
        return list
    }

    suspend fun getOrganizationBasedOnId(id: String) = suspendCancellableCoroutine { cont ->
        database.child(FirebasePaths.Users.node).child(FirebasePaths.Organizations.node).child(id)
            .get().addOnSuccessListener { dataSnapshot ->
                dataSnapshot.getValue<UserDTO>().let { userDTO ->
                    if (userDTO == null)
                        cont.resumeWithException(Exception("Organization not found."))
                    else
                        cont.resume(userDTO) {
                            Exception(it)
                        }
                }
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
    }
}