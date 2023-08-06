package com.sanket.donatetoday.database.firebase.enums

enum class FirebasePaths(val node: String) {
    Users("users"),
    Emails("emails"),
    DonationItems("donation_items"),
    Donors("donors"),
    Organizations("organizations")
}