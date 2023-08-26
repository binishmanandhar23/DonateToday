package com.sanket.donatetoday.modules.home.getters

import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.StatementDTO
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.home.enums.SettingsEnums
import com.sanket.donatetoday.modules.organization.data.OrganizationCashChartData
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorChartData

data class DashboardGetters(
    val userDTO: UserDTO,
    val listOfStatements: List<StatementDTO>,
    val listOfDonationItemUserModel: List<DonationItemUserModel>,
    val organizationCashChartData: List<OrganizationCashChartData>,
    val organizationDonorChartData: List<OrganizationDonorChartData>,
    val onSearchStatements: (String) -> Unit,
    val onStatementClick: (id: String) -> Unit,
    val onEditMonthlyGoal: () -> Unit,
    val onSettingsClick: (SettingsEnums) -> Unit,
    val onDonationItemUserModelClick: (DonationItemUserModel) -> Unit,
    val year: Int,
    val onYearChanged: (year: Int) -> Unit
)
