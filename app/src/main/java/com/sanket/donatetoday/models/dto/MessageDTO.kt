package com.sanket.donatetoday.models.dto

data class MessageDTO(
    val id: String,
    val name: String,
    val email: String,
    val telephoneNo: String,
    val message: String,
    val date: String? = null
)
