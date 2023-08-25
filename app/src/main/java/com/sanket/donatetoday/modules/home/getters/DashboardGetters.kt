package com.sanket.donatetoday.modules.home.getters

import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.StatementDTO
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.home.enums.SettingsEnums
import com.sanket.donatetoday.modules.organization.data.OrganizationCashChartData

data class DashboardGetters(
    val userDTO: UserDTO,
    val listOfStatements: List<StatementDTO>,
    val listOfDonationItemUserModel: List<DonationItemUserModel>,
    val organizationCashChartData: List<OrganizationCashChartData>,
    val onSearchStatements: (String) -> Unit,
    val onEditMonthlyGoal: () -> Unit,
    val onSettingsClick: (SettingsEnums) -> Unit,
    val onDonationItemUserModelClick: (DonationItemUserModel) -> Unit,
    val year: Int,
    val onYearChanged: (year: Int) -> Unit
)
