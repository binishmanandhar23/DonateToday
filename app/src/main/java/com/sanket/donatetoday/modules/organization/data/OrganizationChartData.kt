package com.sanket.donatetoday.modules.organization.data

import org.threeten.bp.LocalDate

data class OrganizationDonorCashChartData(val localDate: LocalDate, val amount: Int)
data class OrganizationDonorChartData(val donor: String, val amount: Int)
