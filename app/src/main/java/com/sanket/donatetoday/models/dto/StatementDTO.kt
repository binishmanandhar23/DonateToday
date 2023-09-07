package com.sanket.donatetoday.models.dto

import com.sanket.donatetoday.modules.organization.data.GenericDonationData
import com.sanket.donatetoday.utils.DateUtils

data class StatementDTO(
    val userId: String = "",
    val organizationId: String = "",
    val userName: String = "",
    val organizationName: String = "",
    val amount: Int? = null,
    val genericDonationData: List<GenericDonationData>? = null,
    val donationType: String = "",
    val date: String = DateUtils.getCurrentDateTime()
)