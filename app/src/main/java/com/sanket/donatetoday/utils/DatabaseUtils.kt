package com.sanket.donatetoday.utils


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.sanket.donatetoday.database.firebase.enums.FirebasePaths
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.EmailDTO
import com.sanket.donatetoday.models.dto.UserDTO

object DatabaseUtils {
    fun DatabaseReference.addListOfEmails(listOfEmailDTO: List<EmailDTO>) =
        this.child(FirebasePaths.Emails.node).setValue(listOfEmailDTO)


    fun DatabaseReference.addUser(userDTO: UserDTO) = this.child(FirebasePaths.Users.node).let {
        when (userDTO.userType) {
            UserType.Donor.type -> it.child(FirebasePaths.Donors.node)
            UserType.Organization.type -> it.child(FirebasePaths.Organizations.node)
            else -> it
        }
    }.child(userDTO.id).setValue(userDTO)

    fun DatabaseReference.addDonationItems(userDTO: UserDTO) =
        this.child(FirebasePaths.DonationItems.node).let { ref ->
            userDTO.donationItemTypes.forEach { donationItem ->
                ref.child(donationItem).let {
                    when (userDTO.userType) {
                        UserType.Donor.type -> it.child(FirebasePaths.Donors.node)
                        UserType.Organization.type -> it.child(FirebasePaths.Organizations.node)
                        else -> it
                    }
                }.child(userDTO.id)
                    .setValue(DonationItemUserModel(id = userDTO.id, name = userDTO.name))
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
}