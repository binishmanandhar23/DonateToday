package com.sanket.donatetoday

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash")
    object OnBoardingScreen: Screen("onboarding")
}
