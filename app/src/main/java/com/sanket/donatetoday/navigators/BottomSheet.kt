package com.sanket.donatetoday.navigators

sealed class BottomSheet(val route: String){
    object MapSheet: BottomSheet("map_bottom_sheet")

    object DonateCashSheet: BottomSheet("donate_cash_bottom_sheet")
    object DonateFoodSheet: BottomSheet("donate_food_bottom_sheet")
    object DonateClothesSheet: BottomSheet("donate_clothes_bottom_sheet")
    object DonateUtensilsSheet: BottomSheet("donate_utensils_bottom_sheet")

    object UserStatementDetail: BottomSheet("user_statement_detail")

    object ChatSheet: BottomSheet("chat_sheet")

    object VerificationScreen: BottomSheet("verification_screen")
}
