package com.sanket.donatetoday.models

interface User {
    var id: String
    var name: String
    var emailAddress: String
    var password: String
    var phoneNo: String?
    var countryPhoneCode: String?
    var userType: String?
    var emailVerified: Boolean
    var userVerified: Boolean
    var totalGoal: Int
    var reached: Int

}