package com.sanket.donatetoday.modules.home.enums

enum class VerificationEnum(val type: String) {
    EMAIL("email"),
    USER("user");

    companion object{
        fun getEnumFromString(type: String?) = when(type){
            EMAIL.type -> EMAIL
            USER.type -> USER
            else -> null
        }
    }
}