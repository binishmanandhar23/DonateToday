package com.sanket.donatetoday.models.dto

data class AllDonationTypeDTO(
    val cash: List<StatementDTO> = emptyList(),
    val food: List<StatementDTO> = emptyList(),
    val clothes: List<StatementDTO> = emptyList(),
    val utensils: List<StatementDTO> = emptyList(),
    val all: List<StatementDTO> = emptyList()
)

fun AllDonationTypeDTO.fillAll() =
    copy(
        all = listOf(
            *cash.toTypedArray(),
            *food.toTypedArray(),
            *clothes.toTypedArray(),
            *utensils.toTypedArray()
        ).sortedBy { it.date }
    )