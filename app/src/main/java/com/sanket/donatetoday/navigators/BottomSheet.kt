package com.sanket.donatetoday.navigators

sealed class BottomSheet(val route: String){
    object MapSheet: BottomSheet("map_bottom_sheet")

    object DonateSheet: BottomSheet("donate_bottom_sheet")

    object UserStatementDetail: BottomSheet("user_statement_detail")

    object ChatSheet: BottomSheet("chat_sheet")
}
