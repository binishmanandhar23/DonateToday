package com.sanket.donatetoday.models.dto

import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.organization.data.ClothesDonationData
import com.sanket.donatetoday.utils.DateUtils

data class StatementDTO(
    val userId: String = "",
    val organizationId: String = "",
    val userName: String = "",
    val organizationName: String = "",
    val amount: Int? = null,
    val clothesDonationData: List<ClothesDonationData>? = null,
    val donationType: String = "",
    val date: String = DateUtils.getCurrentDateTime()
)