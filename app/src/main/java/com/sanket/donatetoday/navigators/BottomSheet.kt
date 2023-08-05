package com.sanket.donatetoday.navigators

sealed class BottomSheet(val route: String){
    object MapSheet: BottomSheet("map_bottom_sheet")
}
