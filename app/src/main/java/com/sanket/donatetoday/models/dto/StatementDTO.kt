package com.sanket.donatetoday.models.dto

import com.sanket.donatetoday.utils.DateUtils

data class StatementDTO(
    val userId: String = "",
    val organizationId: String = "",
    val userName: String = "",
    val organizationName: String = "",
    val amount: Int = 0,
    val date: String = DateUtils.getCurrentDateTime()
)