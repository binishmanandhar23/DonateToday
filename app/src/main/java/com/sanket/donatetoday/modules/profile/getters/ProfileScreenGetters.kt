package com.sanket.donatetoday.modules.profile.getters

import com.sanket.donatetoday.models.dto.UserDTO

data class ProfileScreenGetters(
    val userDTO: UserDTO,
    val onBackButton:() -> Unit,
    val onUpdateProfile: (UserDTO) -> Unit,
    val onAddNewPlace: () -> Unit,
    val onAddDropOffLocation: () -> Unit
)