package com.sanket.donatetoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.sanket.donatetoday.modules.common.dialog.CustomDialog
import com.sanket.donatetoday.modules.common.dialog.enums.DialogTypes
import com.sanket.donatetoday.modules.common.dialog.rememberDialogState
import com.sanket.donatetoday.modules.onboarding.LoginScreenMain
import com.sanket.donatetoday.modules.onboarding.RegistrationScreenMain
import com.sanket.donatetoday.modules.onboarding.SignUpOptionDialog
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.viewmodel.OnBoardingViewModel
import com.sanket.donatetoday.modules.splash.SplashScreen
import com.sanket.donatetoday.navigators.Screen
import com.sanket.donatetoday.navigators.customAnimatedComposable
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import com.sanket.donatetoday.navigators.navigator
import com.sanket.donatetoday.navigators.rememberCustomAnimatedNavController
import com.sanket.donatetoday.ui.theme.DonateTodayTheme
import com.sanket.donatetoday.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel by viewModels<SharedViewModel>()
    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()


    override fun onStart() {
        super.onStart()
        onBoardingViewModel.isUserLoggedIn()
        lifecycleScope.launch {
            onBoardingViewModel.isUserLoggedIn.collect{
                if(!it)
                    sharedViewModel.goToScreen(ScreenNavigator(screen = Screen.OnBoardingScreen))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            val mainNavController = rememberCustomAnimatedNavController()
            val currentScreen by sharedViewModel.currentScreen.collectAsState()
            val customDialogState = rememberDialogState()

            LaunchedEffect(key1 = currentScreen) {
                val clearBackStack = when (currentScreen?.screen) {
                    Screen.OnBoardingScreen -> true
                    else -> false
                }
                val clearBackStackToRoute = when (currentScreen?.screen) {
                    Screen.OnBoardingScreen -> Screen.SplashScreen.route
                    else -> null
                }
                if (currentScreen != null)
                    mainNavController.navigator(
                        route = currentScreen!!.screen.route.let { route ->
                            if (currentScreen?.values.isNullOrEmpty())
                                route
                            else
                                route + "/${currentScreen?.values?.joinToString("/")}"
                        },
                        clearBackStack = clearBackStack,
                        clearBackStackToRoute = clearBackStackToRoute
                    )
            }

            DonateTodayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CustomDialog(
                        customDialogState = customDialogState,
                        dialogContent = { dialogType, extraString ->
                            when (dialogType) {
                                DialogTypes.SignUpOption -> SignUpOptionDialog(asDonor = {
                                    onBoardingViewModel.updateUserData(userType = UserType.Donor)
                                    sharedViewModel.goToScreen(
                                        screenNavigator = ScreenNavigator(
                                            screen = Screen.RegistrationScreen, values = listOf(
                                                UserType.Donor.type
                                            )
                                        )
                                    )
                                    customDialogState.hide()
                                }, asOrganization = {
                                    onBoardingViewModel.updateUserData(userType = UserType.Organization)
                                    sharedViewModel.goToScreen(
                                        screenNavigator = ScreenNavigator(
                                            screen = Screen.RegistrationScreen, values = listOf(
                                                UserType.Organization.type
                                            )
                                        )
                                    )
                                    customDialogState.hide()
                                })

                                else -> Unit
                            }
                        }) {
                        MainNavHost(
                            mainNavController = mainNavController,
                            currentScreen = currentScreen?.screen,
                            onSignUp = {
                                customDialogState.show(dialog = DialogTypes.SignUpOption)
                            }
                        )
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainNavHost(
        mainNavController: NavHostController,
        currentScreen: Screen?,
        startDestination: Screen = Screen.SplashScreen,
        onSignUp: () -> Unit
    ) {

        AnimatedNavHost(
            navController = mainNavController,
            startDestination = startDestination.route
        ) {
            customAnimatedComposable(route = Screen.SplashScreen.route) {
                SplashScreen {
                    sharedViewModel.goToScreen(ScreenNavigator(screen = Screen.OnBoardingScreen))
                }
            }
            customAnimatedComposable(route = Screen.OnBoardingScreen.route) {
                val user by onBoardingViewModel.user.collectAsState()
                LoginScreenMain(
                    emailAddress = user.emailAddress,
                    password = user.password,
                    onEmailAddressChanged = {
                        onBoardingViewModel.updateUserData(emailAddress = it)
                    },
                    onPasswordChanged = {
                        onBoardingViewModel.updateUserData(password = it)
                    },
                    onSignIn = {
                        
                    }, onSignUp = onSignUp
                )
            }

            customAnimatedComposable(route = Screen.RegistrationScreen.route + "/{${UserType::class.java.simpleName}}") { navBackStackEntry ->
                UserType.getRegisterAs(navBackStackEntry.arguments?.getString(UserType::class.java.simpleName))
                    ?.let { registerAs ->
                        val user by onBoardingViewModel.user.collectAsState()
                        RegistrationScreenMain(user = user, onBack = {
                            mainNavController.popBackStack()
                        }, onUpdate = {
                            onBoardingViewModel.updateUserData(user = it)
                        })
                    }
            }
        }
    }
}