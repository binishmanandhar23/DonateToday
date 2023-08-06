package com.sanket.donatetoday.models.dto

import com.google.firebase.database.IgnoreExtraProperties
import com.sanket.donatetoday.models.User
import com.sanket.donatetoday.models.entity.UserEntity
import io.realm.kotlin.ext.toRealmList
import java.util.UUID


data class UserDTO(
    override var id: String = UUID.randomUUID().toString(),
    override var name: String = "",
    override var emailAddress: String = "",
    override var password: String = "",
    override var phoneNo: String? = null,
    override var countryPhoneCode: String? = null,
    val cardInfo: CreditCardDataDTO? = null,
    val donationItemTypes: List<String> = emptyList(),
    override var userType: String? = null,
    override var verified: Boolean = false,
    override var totalGoal: Int = 0,
    override var reached: Int = 0,
) : User


fun UserDTO.toUserEntity() = UserEntity(
    id = id,
    name = name,
    emailAddress = emailAddress,
    password = password,
    countryPhoneCode = countryPhoneCode,
    cardInfo = cardInfo?.toCreditCardDataEntity(),
    donationItemTypes = donationItemTypes.toRealmList(),
    userType = userType,
    verified = verified,
    totalGoal = totalGoal,
    reached = reached
)

fun UserEntity.toUserDTO() = UserDTO(
    id = id,
    name = name,
    emailAddress = emailAddress,
    password = password,
    countryPhoneCode = countryPhoneCode,
    cardInfo = cardInfo?.toCreditCardDataDTO(),
    donationItemTypes = donationItemTypes.toList(),
    userType = userType,
    verified = verified,
    totalGoal = totalGoal,
    reached = reached
)

