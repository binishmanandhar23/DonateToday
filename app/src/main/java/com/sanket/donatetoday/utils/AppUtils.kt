package com.sanket.donatetoday.utils

import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.CreditCardDataDTO


const val MaximumMonthlyGoal = 10000

fun String?.emptyIfNull() = this ?: ""

fun String?.getInitial(): String? = this?.getOrNull(0)?.toString()

fun String?.verifyEmptyOrNull() = isNullOrEmpty()

fun List<String>?.verifyEmptyOrNull() = isNullOrEmpty()
fun CreditCardDataDTO?.verifyEmptyOrNull(userType: String?) = (userType == UserType.Organization.type && (this?.cardHolderName.isNullOrEmpty() || this?.cardNo.isNullOrEmpty() || this?.expiresOn.isNullOrEmpty()))