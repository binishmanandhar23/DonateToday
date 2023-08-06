package com.sanket.donatetoday.modules.home.getters

import com.sanket.donatetoday.models.dto.UserDTO

data class DashboardGetters(val userDTO: UserDTO, val onEditMonthlyGoal: () -> Unit)
