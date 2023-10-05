package com.sanket.donatetoday.models.dto

import com.google.firebase.database.Exclude
import com.sanket.donatetoday.models.User
import com.sanket.donatetoday.models.entity.UserEntity
import io.realm.kotlin.ext.toRealmList
import java.util.UUID


data class UserDTO(
    override var id: String = UUID.randomUUID().toString(),
    override var name: String = "",
    override var emailAddress: String = "",
    override var password: String = "",
    @Exclude
    var confirmPassword: String = "",
    override var phoneNo: String? = null,
    override var countryPhoneCode: String? = null,
    val cardInfo: CreditCardDataDTO? = null,
    val donationItemTypes: List<String> = emptyList(),
    val location: LocationDTO = LocationDTO(),
    val dropOffLocations: List<LocationDTO> = emptyList(),
    override var userType: String? = null,
    override var emailVerified: Boolean = false,
    override var userVerified: Boolean = false,
    override var totalGoal: Int = 0,
    override var reached: Int = 0,
) : User


fun UserDTO.toUserEntity() = UserEntity(
    id = id,
    name = name,
    emailAddress = emailAddress,
    password = password,
    countryPhoneCode = countryPhoneCode,
    phoneNo = phoneNo,
    cardInfo = cardInfo?.toCreditCardDataEntity(),
    donationItemTypes = donationItemTypes.toRealmList(),
    location = location.toLocationEntity(),
    dropOffLocations = dropOffLocations.toRealmListOfLocationEntity(),
    userType = userType,
    emailVerified = emailVerified,
    userVerified = userVerified,
    totalGoal = totalGoal,
    reached = reached
)

fun UserEntity.toUserDTO(verified: Boolean? = null) = UserDTO(
    id = id,
    name = name,
    emailAddress = emailAddress,
    password = password,
    countryPhoneCode = countryPhoneCode,
    phoneNo = phoneNo,
    cardInfo = cardInfo?.toCreditCardDataDTO(),
    donationItemTypes = donationItemTypes.toList(),
    location = location?.toLocationDTO()?: LocationDTO(),
    dropOffLocations = dropOffLocations.toListOfLocationDTO(),
    userType = userType,
    emailVerified = verified?: this.emailVerified,
    userVerified = userVerified,
    totalGoal = totalGoal,
    reached = reached
)

fun UserDTO.verifyOrganization() = emailVerified && userVerified

