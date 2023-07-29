package com.sanket.donatetoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.sanket.donatetoday.modules.onboarding.RegistrationScreenMain
import com.sanket.donatetoday.modules.splash.SplashScreen
import com.sanket.donatetoday.navigators.customAnimatedComposable
import com.sanket.donatetoday.navigators.navigator
import com.sanket.donatetoday.navigators.rememberCustomAnimatedNavController
import com.sanket.donatetoday.ui.theme.DonateTodayTheme
import com.sanket.donatetoday.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    private val sharedViewModel by viewModels<SharedViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            val mainNavController = rememberCustomAnimatedNavController()
            val currentScreen by sharedViewModel.currentScreen.collectAsState()

            LaunchedEffect(key1 = currentScreen) {
                val clearBackStack = when(currentScreen){
                    Screen.OnBoardingScreen -> true
                    else -> false
                }
                val clearBackStackToRoute = when(currentScreen){
                    Screen.OnBoardingScreen -> Screen.SplashScreen.route
                    else -> null
                }
                if (currentScreen != null)
                    mainNavController.navigator(
                        route = currentScreen!!.route,
                        clearBackStack = clearBackStack,
                        clearBackStackToRoute = clearBackStackToRoute
                    )
            }

            DonateTodayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainNavHost(
                        mainNavController = mainNavController,
                        currentScreen = currentScreen
                    )
                }
            }
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainNavHost(
        mainNavController: NavHostController,
        currentScreen: Screen?,
        startDestination: Screen = Screen.SplashScreen
    ) {

        AnimatedNavHost(
            navController = mainNavController,
            startDestination = startDestination.route
        ) {
            customAnimatedComposable(route = Screen.SplashScreen.route) {
                SplashScreen {
                    sharedViewModel.goToScreen(Screen.OnBoardingScreen)
                }
            }
            customAnimatedComposable(route = Screen.OnBoardingScreen.route) {
                RegistrationScreenMain()
            }
        }
    }
}