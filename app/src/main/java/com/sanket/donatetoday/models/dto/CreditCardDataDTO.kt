package com.sanket.donatetoday.models.dto

import com.sanket.donatetoday.models.CreditCardData
import com.sanket.donatetoday.models.entity.CreditCardDataEntity

data class CreditCardDataDTO(
    override var cardNo: String = "",
    override var cardHolderName: String = "",
    override var expiresOn: String = ""
): CreditCardData


fun CreditCardDataDTO.toCreditCardDataEntity() =
    CreditCardDataEntity(cardNo = cardNo, cardHolderName = cardHolderName, expiresOn = expiresOn)

fun CreditCardDataEntity.toCreditCardDataDTO() =
    CreditCardDataDTO(cardNo = cardNo, cardHolderName = cardHolderName, expiresOn = expiresOn)