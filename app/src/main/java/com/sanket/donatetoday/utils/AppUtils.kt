package com.sanket.donatetoday.utils

import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.CreditCardDataDTO
import com.sanket.donatetoday.models.dto.UserDTO


const val MaximumMonthlyGoal = 10000

fun String?.emptyIfNull() = this ?: ""

fun String?.getInitial(): String? = this?.getOrNull(0)?.toString()

fun String?.verifyEmptyOrNull() = isNullOrEmpty()

fun List<String>?.verifyEmptyOrNull() = isNullOrEmpty()
fun CreditCardDataDTO?.verifyEmptyOrNull(userType: String?) = (userType == UserType.Organization.type && (this?.cardHolderName.isNullOrEmpty() || this?.cardNo.isNullOrEmpty() || this?.expiresOn.isNullOrEmpty()))

fun String.isValidPassword(): Boolean {
    if (this.length < 8) return false
    if (this.firstOrNull { it.isDigit() } == null) return false
    if (this.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
    if (this.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
    if (this.firstOrNull { !it.isLetterOrDigit() } == null) return false

    return true
}
