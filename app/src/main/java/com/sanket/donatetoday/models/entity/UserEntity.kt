package com.sanket.donatetoday.models.entity

import com.sanket.donatetoday.models.User
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class UserEntity : RealmObject, User {
    override var id: String = ""
    override var name: String = ""
    override var emailAddress: String = ""
    override var password: String = ""
    override var phoneNo: String? = null
    override var countryPhoneCode: String? = null
    var cardInfo: CreditCardDataEntity? = null
    var donationItemTypes: RealmList<String> = realmListOf()
    override var userType: String? = null
    override var verified: Boolean = false

    constructor(
        id: String,
        name: String,
        emailAddress: String,
        password: String,
        countryPhoneCode: String? = null,
        cardInfo: CreditCardDataEntity? = null,
        donationItemTypes: RealmList<String> = realmListOf(),
        userType: String? = null,
        verified: Boolean = false
    ): super() {
        this.id = id
        this.name = name
        this.emailAddress = emailAddress
        this.password = password
        this.countryPhoneCode = countryPhoneCode
        this.cardInfo = cardInfo
        this.donationItemTypes = donationItemTypes
        this.userType = userType
        this.verified = verified
    }

    constructor(): super()
}