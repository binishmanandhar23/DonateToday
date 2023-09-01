package com.sanket.donatetoday.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.sanket.donatetoday.database.firebase.enums.FirebasePaths
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.AllDonationTypeDTO
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.StatementDTO
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.fillAll
import com.sanket.donatetoday.models.entity.UserEntity
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.organization.data.ClothesDonationData
import com.sanket.donatetoday.utils.DatabaseUtils.getStatements
import com.sanket.donatetoday.utils.DatabaseUtils.getStatementsAsynchronously
import com.sanket.donatetoday.utils.DatabaseUtils.getUser
import com.sanket.donatetoday.utils.DatabaseUtils.updateReachedAmount
import com.sanket.donatetoday.utils.DatabaseUtils.updateUser
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

    suspend fun getUserBasedOnId(id: String) = suspendCancellableCoroutine { cont ->
        database.child(FirebasePaths.Users.node).child(FirebasePaths.Donors.node).child(id)
            .get().addOnSuccessListener { dataSnapshot ->
                dataSnapshot.getValue<UserDTO>().let { userDTO ->
                    if (userDTO == null)
                        cont.resumeWithException(Exception("User not found."))
                    else
                        cont.resume(userDTO) {
                            Exception(it)
                        }
                }
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
    }

    suspend fun getUserFromFirebase(email: String?) = suspendCancellableCoroutine { cont ->
        if (email == null)
            cont.resumeWithException(Exception("User not found."))
        else
            database.getUser(email = email, onSuccess = { userDTO ->
                cont.resume(userDTO) {
                    Exception(it)
                }
            }, onError = {
                cont.resumeWithException(Exception(it))
            })
    }

    suspend fun getStatementsFromFirebase(userDTO: UserDTO) = suspendCancellableCoroutine { cont ->
        database.getStatements(userDTO = userDTO, onSuccess = { allDonationTypeDTO ->
            cont.resume(allDonationTypeDTO.fillAll()) {
                Exception(it)
            }
        }, onError = {
            cont.resumeWithException(Exception(it))
        })
    }

    suspend fun updateUserInFirebase(userDTO: UserDTO) = suspendCancellableCoroutine { cont ->
        database.updateUser(userDTO = userDTO, onSuccess = { userDTO ->
            cont.resume(userDTO) {
                Exception(it)
            }
        }, onError = {
            cont.resumeWithException(Exception(it))
        })
    }

    suspend fun addCashDonation(userDTO: UserDTO, organization: UserDTO, amount: Int) =
        suspendCancellableCoroutine { cont ->
            database.child(FirebasePaths.Statements.node).child(FirebasePaths.Donated.node)
                .child(userDTO.id).child(DonationItemTypes.Cash.type).get()
                .addOnSuccessListener { dataSnapshot ->
                    dataSnapshot.getValue<List<StatementDTO>>().let { list ->
                        val newList = if (list.isNullOrEmpty())
                            listOf(
                                StatementDTO(
                                    userId = userDTO.id,
                                    organizationId = organization.id,
                                    userName = userDTO.name,
                                    organizationName = organization.name,
                                    amount = amount,
                                    donationType = DonationItemTypes.Cash.type
                                )
                            )
                        else
                            list.toMutableList().let {
                                it.add(
                                    StatementDTO(
                                        userId = userDTO.id,
                                        organizationId = organization.id,
                                        userName = userDTO.name,
                                        organizationName = organization.name,
                                        amount = amount,
                                        donationType = DonationItemTypes.Cash.type
                                    )
                                )
                                it
                            }
                        dataSnapshot.ref.setValue(newList)

                        database.child(FirebasePaths.Statements.node)
                            .child(FirebasePaths.Received.node)
                            .child(organization.id).child(DonationItemTypes.Cash.type).get()
                            .addOnSuccessListener { dataSnapshot ->
                                dataSnapshot.getValue<List<StatementDTO>>().let { list ->
                                    val newList = if (list.isNullOrEmpty())
                                        listOf(
                                            StatementDTO(
                                                userId = userDTO.id,
                                                organizationId = organization.id,
                                                userName = userDTO.name,
                                                organizationName = organization.name,
                                                amount = amount,
                                                donationType = DonationItemTypes.Cash.type
                                            )
                                        )
                                    else
                                        list.toMutableList().let {
                                            it.add(
                                                StatementDTO(
                                                    userId = userDTO.id,
                                                    organizationId = organization.id,
                                                    userName = userDTO.name,
                                                    organizationName = organization.name,
                                                    amount = amount,
                                                    donationType = DonationItemTypes.Cash.type
                                                )
                                            )
                                            it
                                        }
                                    dataSnapshot.ref.setValue(newList)
                                    database.updateReachedAmount(
                                        userDTO = userDTO,
                                        organizationDTO = organization,
                                        amount = amount,
                                        onSuccess = {
                                            cont.resume(true) {
                                                Exception(it)
                                            }
                                        }, onError = {
                                            cont.resumeWithException(it)
                                        })
                                }
                            }.addOnFailureListener {
                                cont.resumeWithException(it)
                            }
                    }
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }

    suspend fun addClothesDonation(
        userDTO: UserDTO,
        organization: UserDTO,
        clothesDonationData: List<ClothesDonationData>
    ) =
        suspendCancellableCoroutine { cont ->
            database.child(FirebasePaths.Statements.node).child(FirebasePaths.Donated.node)
                .child(userDTO.id).child(DonationItemTypes.Clothes.type).get()
                .addOnSuccessListener { dataSnapshot ->
                    dataSnapshot.getValue<List<StatementDTO>>().let { list ->
                        val newList = if (list.isNullOrEmpty())
                            listOf(
                                StatementDTO(
                                    userId = userDTO.id,
                                    organizationId = organization.id,
                                    userName = userDTO.name,
                                    organizationName = organization.name,
                                    clothesDonationData = clothesDonationData,
                                    donationType = DonationItemTypes.Clothes.type
                                )
                            )
                        else
                            list.toMutableList().let {
                                it.add(
                                    StatementDTO(
                                        userId = userDTO.id,
                                        organizationId = organization.id,
                                        userName = userDTO.name,
                                        organizationName = organization.name,
                                        clothesDonationData = clothesDonationData,
                                        donationType = DonationItemTypes.Clothes.type
                                    )
                                )
                                it
                            }
                        dataSnapshot.ref.setValue(newList)

                        database.child(FirebasePaths.Statements.node)
                            .child(FirebasePaths.Received.node)
                            .child(organization.id).child(DonationItemTypes.Cash.type).get()
                            .addOnSuccessListener { dataSnapshot ->
                                dataSnapshot.getValue<List<StatementDTO>>().let { list ->
                                    val newList = if (list.isNullOrEmpty())
                                        listOf(
                                            StatementDTO(
                                                userId = userDTO.id,
                                                organizationId = organization.id,
                                                userName = userDTO.name,
                                                organizationName = organization.name,
                                                donationType = DonationItemTypes.Clothes.type,
                                                clothesDonationData = clothesDonationData
                                            )
                                        )
                                    else
                                        list.toMutableList().let {
                                            it.add(
                                                StatementDTO(
                                                    userId = userDTO.id,
                                                    organizationId = organization.id,
                                                    userName = userDTO.name,
                                                    organizationName = organization.name,
                                                    donationType = DonationItemTypes.Clothes.type,
                                                    clothesDonationData = clothesDonationData
                                                )
                                            )
                                            it
                                        }
                                    dataSnapshot.ref.setValue(newList)
                                }
                            }.addOnFailureListener {
                                cont.resumeWithException(it)
                            }
                    }
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }


    fun getStatementsAsynchronously(
        user: UserDTO,
        onSuccess: (AllDonationTypeDTO) -> Unit,
        onError: (String) -> Unit
    ) =
        database.getStatementsAsynchronously(userDTO = user, onDataChange = { snapshot ->
            onSuccess((snapshot.getValue<AllDonationTypeDTO>() ?: AllDonationTypeDTO()).fillAll())
        }, onCancelled = {
            onError(it.message)
        })
}