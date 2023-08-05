package com.sanket.donatetoday.models

import com.sanket.donatetoday.modules.common.data.CreditCardData
import java.util.UUID

data class User(
     val id: String = UUID.randomUUID().toString(),
     val name: String = "",
     val emailAddress: String = "",
     val password: String = "",
     val phoneNo: String? = null,
     val countryPhoneCode: String? = null,
     val cardInfo: CreditCardData? = null,
     val donationItemTypes: List<String> = emptyList(),
     val userType: String? = null,

     @Transient
     val refreshId: UUID = UUID.randomUUID()
)