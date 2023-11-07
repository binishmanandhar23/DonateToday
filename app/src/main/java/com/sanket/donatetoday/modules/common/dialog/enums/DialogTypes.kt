package com.sanket.donatetoday.modules.common.dialog.enums

enum class DialogTypes(val type: String) {
    None("None"),
    MonthlyGoal("monthly_goal"),
    SignUpOption("sign_up_option"),
    DeleteUser("delete_user")
    ;

    companion object {
        fun getValue(type: String?) = when(type){
            SignUpOption.type -> SignUpOption
            else -> None
        }
    }
}