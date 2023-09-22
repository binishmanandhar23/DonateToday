package com.sanket.donatetoday.models.entity

import com.sanket.donatetoday.models.User
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserEntity : RealmObject, User {
    @PrimaryKey
    override var id: String = ""
    override var name: String = ""
    override var emailAddress: String = ""
    override var password: String = ""
    override var phoneNo: String? = null
    override var countryPhoneCode: String? = null
    var cardInfo: CreditCardDataEntity? = null
    var donationItemTypes: RealmList<String> = realmListOf()
    override var userType: String? = null
    override var emailVerified: Boolean = false
    override var userVerified: Boolean = false
    override var totalGoal: Int = 0
    override var reached: Int = 0

    constructor(
        id: String,
        name: String,
        emailAddress: String,
        password: String,
        countryPhoneCode: String? = null,
        phoneNo: String? = null,
        cardInfo: CreditCardDataEntity? = null,
        donationItemTypes: RealmList<String> = realmListOf(),
        userType: String? = null,
        emailVerified: Boolean = false,
        userVerified: Boolean = false,
        totalGoal: Int = 0,
        reached: Int = 0
    ): super() {
        this.id = id
        this.name = name
        this.emailAddress = emailAddress
        this.password = password
        this.countryPhoneCode = countryPhoneCode
        this.phoneNo = phoneNo
        this.cardInfo = cardInfo
        this.donationItemTypes = donationItemTypes
        this.userType = userType
        this.emailVerified = emailVerified
        this.userVerified = userVerified
        this.totalGoal = totalGoal
        this.reached = reached
    }

    constructor(): super()
}