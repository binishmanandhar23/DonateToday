package com.sanket.donatetoday.modules.onboarding.enums

enum class RegisterAs(val registerAs: String) {
    Donor("donor"),
    Organization("organization");

    companion object {
        fun getRegisterAs(string: String?) = when (string) {
            Donor.registerAs -> Donor
            Organization.registerAs -> Organization
            else -> null
        }
    }
}