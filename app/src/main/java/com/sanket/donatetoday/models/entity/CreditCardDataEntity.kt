package com.sanket.donatetoday.models.entity

import com.sanket.donatetoday.models.CreditCardData
import io.realm.kotlin.types.RealmObject

class CreditCardDataEntity: RealmObject, CreditCardData {
    override var cardNo: String = ""
        
    override var cardHolderName: String = ""

    override var expiresOn: String = ""
        
    constructor(cardNo: String, cardHolderName: String, expiresOn: String): super(){
        this.cardNo = cardNo
        this.cardHolderName = cardHolderName
        this.expiresOn = expiresOn
    }

    constructor(): super()
}