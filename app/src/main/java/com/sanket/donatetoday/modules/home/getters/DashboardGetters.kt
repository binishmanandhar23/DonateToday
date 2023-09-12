package com.sanket.donatetoday.modules.home.getters

import com.sanket.donatetoday.models.dto.AllDonationTypeDTO
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.home.enums.SettingsEnums
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorCashChartData
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorChartData

data class DashboardGetters(
    val userDTO: UserDTO,
    val listOfAllStatements: AllDonationTypeDTO,
    val listOfDonationItemUserModel: List<DonationItemUserModel>,
    val organizationCashChartData: List<OrganizationDonorCashChartData>,
    val donorCashChartData: List<OrganizationDonorCashChartData>,
    val organizationDonorChartData: List<OrganizationDonorChartData>,
    val onSearchStatements: (String) -> Unit,
    val onStatementClick: (id: String) -> Unit,
    val onEditMonthlyGoal: () -> Unit,
    val onSettingsClick: (SettingsEnums) -> Unit,
    val onOrganizationClick: (String) -> Unit,
    val year: Int,
    val onYearChanged: (year: Int) -> Unit,
    val selectedStatementTypes: DonationItemTypes?,
    val onStatementTypeSelected: (DonationItemTypes?) -> Unit,
    val onVerificationRequired: () -> Unit,
)
