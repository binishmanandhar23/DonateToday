package com.sanket.donatetoday.utils


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.sanket.donatetoday.database.firebase.enums.FirebasePaths
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.AllDonationTypeDTO
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.EmailDTO
import com.sanket.donatetoday.models.dto.StatementDTO
import com.sanket.donatetoday.models.dto.UserDTO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

object DatabaseUtils {
    fun DatabaseReference.addListOfEmails(listOfEmailDTO: List<EmailDTO>) =
        this.child(FirebasePaths.Emails.node).setValue(listOfEmailDTO)


    fun DatabaseReference.addUser(
        userDTO: UserDTO,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) = this.child(FirebasePaths.Users.node).let {
        when (userDTO.userType) {
            UserType.Donor.type -> it.child(FirebasePaths.Donors.node)
            UserType.Organization.type -> it.child(FirebasePaths.Organizations.node)
            else -> it
        }
    }.child(userDTO.id).setValue(userDTO).addOnSuccessListener {
        onSuccess?.invoke()
    }.addOnFailureListener {
        onError?.invoke()
    }

    fun DatabaseReference.getUser(
        userDTO: UserDTO,
        onSuccess: (UserDTO?) -> Unit,
        onError: (String) -> Unit
    ) = this.child(FirebasePaths.Users.node).let {
        when (userDTO.userType) {
            UserType.Donor.type -> it.child(FirebasePaths.Donors.node)
            UserType.Organization.type -> it.child(FirebasePaths.Organizations.node)
            else -> it
        }
    }.child(userDTO.id).addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            onSuccess(snapshot.getValue<UserDTO>())
        }

        override fun onCancelled(error: DatabaseError) {
            onError(error.message)
        }
    })

    fun DatabaseReference.getAllOrganizations(
        onSuccess: (List<UserDTO>) -> Unit,
        onError: (String?) -> Unit
    ) = this.child(FirebasePaths.Users.node).child(FirebasePaths.Organizations.node).addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            onSuccess(snapshot.getValue<HashMap<String, UserDTO>>()?.map { it.value }?: emptyList())
        }

        override fun onCancelled(error: DatabaseError) {
            onError(error.message)
        }
    })

    fun DatabaseReference.addDonationItems(userDTO: UserDTO) =
        this.child(FirebasePaths.DonationItems.node).let { ref ->
            userDTO.donationItemTypes.forEach { donationItem ->
                ref.child(donationItem).child(userDTO.userType!!).get()
                    .addOnSuccessListener { dataSnapshot ->
                        (dataSnapshot.getValue<List<DonationItemUserModel>>()).let { listOfDonationItemUserModel ->
                            val list = if (listOfDonationItemUserModel.isNullOrEmpty()) {
                                listOf(
                                    DonationItemUserModel(
                                        id = userDTO.id,
                                        name = userDTO.name
                                    )
                                )
                            } else {
                                listOfDonationItemUserModel.toMutableList().let {
                                    it.add(
                                        DonationItemUserModel(
                                            id = userDTO.id,
                                            name = userDTO.name
                                        )
                                    )
                                    it
                                }
                            }
                            dataSnapshot.ref.setValue(list)
                        }
                    }
            }
        }

    fun DatabaseReference.getUser(
        email: String,
        onSuccess: (UserDTO) -> Unit,
        onError: (String?) -> Unit
    ) {
        this.child(FirebasePaths.Emails.node).get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.getValue<List<EmailDTO>>()?.let { listOfEmailDTO ->
                listOfEmailDTO.find { it.email == email }?.let { emailDTO ->
                    emailDTO.userType?.let { userType ->
                        this.child(FirebasePaths.Users.node)
                            .child(if (userType == UserType.Donor.type) FirebasePaths.Donors.node else FirebasePaths.Organizations.node)
                            .child(emailDTO.id)
                            .get().addOnSuccessListener { userSnapshot ->
                                userSnapshot.getValue<UserDTO>()?.let {
                                    onSuccess(it)
                                }
                            }.addOnFailureListener {
                                onError(it.message)
                            }
                    }
                }
            }
        }.addOnFailureListener {
            onError(it.message)
        }
    }

    fun DatabaseReference.updateUser(
        userDTO: UserDTO,
        onSuccess: (UserDTO) -> Unit,
        onError: (String?) -> Unit
    ) {
        this.child(FirebasePaths.Users.node).child(
            when (userDTO.userType) {
                UserType.Organization.type -> FirebasePaths.Organizations.node
                else -> FirebasePaths.Donors.node
            }
        ).child(userDTO.id).setValue(userDTO).addOnSuccessListener {
            onSuccess(userDTO)
        }.addOnFailureListener {
            onError(it.message)
        }
    }

    fun DatabaseReference.getStatements(
        userDTO: UserDTO,
        onSuccess: (AllDonationTypeDTO) -> Unit,
        onError: (String?) -> Unit
    ) {
        this.child(FirebasePaths.Statements.node)
            .child(if (userDTO.userType == UserType.Donor.type) FirebasePaths.Donated.node else FirebasePaths.Received.node)
            .child(userDTO.id)
            .get().addOnSuccessListener { dataSnapshot ->
                dataSnapshot.getValue<AllDonationTypeDTO>()?.let { allDonationTypeDTO ->
                    onSuccess(allDonationTypeDTO)
                }
            }.addOnFailureListener {
                onError(it.message)
            }
    }

    fun DatabaseReference.getStatementsAsynchronously(
        userDTO: UserDTO,
        onDataChange: (snapshot: DataSnapshot) -> Unit,
        onCancelled: (error: DatabaseError) -> Unit
    ) {
        this.child(FirebasePaths.Statements.node)
            .child(if (userDTO.userType == UserType.Donor.type) FirebasePaths.Donated.node else FirebasePaths.Received.node)
            .child(userDTO.id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onDataChange(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    onCancelled(error)
                }
            })
    }

    fun DatabaseReference.updateReachedAmount(
        userDTO: UserDTO,
        organizationDTO: UserDTO,
        amount: Int,
        onSuccess: () -> Unit,
        onError: ((Exception) -> Unit),
    ) {
        child(FirebasePaths.Users.node).child(FirebasePaths.Donors.node).child(userDTO.id)
            .setValue(userDTO.copy(reached = userDTO.reached + amount)).addOnSuccessListener {
                child(FirebasePaths.Users.node).child(FirebasePaths.Organizations.node)
                    .child(organizationDTO.id)
                    .setValue(organizationDTO.copy(reached = organizationDTO.reached + amount))
                    .addOnSuccessListener {
                        onSuccess()
                    }.addOnFailureListener {
                        onError(it)
                    }
            }.addOnFailureListener {
                onError(it)
            }
    }
}