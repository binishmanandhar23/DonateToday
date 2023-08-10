package com.sanket.donatetoday.modules.home.getters

import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.home.enums.SettingsEnums

data class DashboardGetters(val userDTO: UserDTO, val onEditMonthlyGoal: () -> Unit, val onSettingsClick:(SettingsEnums) -> Unit)
