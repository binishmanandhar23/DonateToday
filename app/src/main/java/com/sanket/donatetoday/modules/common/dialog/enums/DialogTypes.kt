package com.sanket.donatetoday.modules.common.dialog.enums

enum class DialogTypes(val type: String) {
    None("None"),
    SignUpOption("sign_up_option");

    companion object {
        fun getValue(type: String?) = when(type){
            SignUpOption.type -> SignUpOption
            else -> None
        }
    }
}