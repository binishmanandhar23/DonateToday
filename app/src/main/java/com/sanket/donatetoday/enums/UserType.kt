package com.sanket.donatetoday.enums

enum class UserType(val type: String) {
    Donor("donor"),
    Organization("organization");

    companion object {
        fun getRegisterAs(string: String?) = when (string) {
            Donor.type -> Donor
            Organization.type -> Organization
            else -> null
        }
    }
}