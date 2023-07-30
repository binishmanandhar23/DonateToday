package com.sanket.donatetoday.modules.onboarding.data

import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.onboarding.enums.RegisterAs

data class RegistrationData(
    val registerAs: RegisterAs? = null,
    val emailAddress: String = "",
    val password: String = "",
    val name: String = "",
    val phoneNo: String = "",
    val cardNo: String = "",
    val donationItemTypes: List<DonationItemTypes> = emptyList(),
)
