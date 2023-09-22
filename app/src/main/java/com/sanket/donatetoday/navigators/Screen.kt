package com.sanket.donatetoday.navigators

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash")
    object OnBoardingScreen: Screen("onboarding")

    object RegistrationScreen: Screen("registration_screen")

    object HomeScreen: Screen("home_screen")

    object OrganizationDetail: Screen("organization_detail")

    object ProfileScreen: Screen("profile")

    object AllOrganizationsList: Screen("all_organizations_list")

    object MapSheet: Screen("map_screen")
}
