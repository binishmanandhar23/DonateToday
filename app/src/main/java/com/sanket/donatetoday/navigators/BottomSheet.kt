package com.sanket.donatetoday.navigators

sealed class BottomSheet(val route: String){
    object MapSheet: BottomSheet("map_bottom_sheet")

    object DonateSheet: BottomSheet("donate_bottom_sheet")
}
