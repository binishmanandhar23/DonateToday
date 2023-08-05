package com.sanket.donatetoday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.sanket.donatetoday.modules.common.dialog.CustomDialog
import com.sanket.donatetoday.modules.common.dialog.enums.DialogTypes
import com.sanket.donatetoday.modules.common.dialog.rememberDialogState
import com.sanket.donatetoday.modules.onboarding.LoginScreenMain
import com.sanket.donatetoday.modules.onboarding.RegistrationScreenMain
import com.sanket.donatetoday.modules.onboarding.SignUpOptionDialog
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.modules.common.map.DonateTodayMap
import com.sanket.donatetoday.modules.common.snackbar.CustomSnackBar
import com.sanket.donatetoday.modules.common.snackbar.SnackBarLengthLong
import com.sanket.donatetoday.modules.common.snackbar.SnackBarState
import com.sanket.donatetoday.modules.common.snackbar.rememberSnackBarState
import com.sanket.donatetoday.viewmodel.OnBoardingViewModel
import com.sanket.donatetoday.modules.splash.SplashScreen
import com.sanket.donatetoday.navigators.BottomSheet
import com.sanket.donatetoday.navigators.Screen
import com.sanket.donatetoday.navigators.customAnimatedComposable
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import com.sanket.donatetoday.navigators.navigator
import com.sanket.donatetoday.navigators.rememberCustomAnimatedNavController
import com.sanket.donatetoday.navigators.rememberCustomBottomSheetNavigator
import com.sanket.donatetoday.ui.states.LoginUIState
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
    }

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            val bottomSheetNavigator = rememberCustomBottomSheetNavigator()
            val mainNavController = rememberCustomAnimatedNavController(bottomSheetNavigator)
            val currentScreen by sharedViewModel.currentScreen.collectAsState()
            val customDialogState = rememberDialogState()
            val customSnackBarState = rememberSnackBarState()

            Launchers(
                mainNavController = mainNavController,
                currentScreen = currentScreen,
                snackBarState = customSnackBarState
            )

            DonateTodayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    CustomSnackBar(
                        text = "",
                        snackBarState = customSnackBarState,
                        useBox = true
                    ) {
                        CustomDialog(
                            customDialogState = customDialogState,
                            dialogContent = { dialogType, extraString ->
                                when (dialogType) {
                                    DialogTypes.SignUpOption -> SignUpOptionDialog(asDonor = {
                                        onBoardingViewModel.updateUserData(userType = UserType.Donor)
                                        sharedViewModel.goToScreen(
                                            screenNavigator = ScreenNavigator(
                                                screen = Screen.RegistrationScreen,
                                                values = listOf(
                                                    UserType.Donor.type
                                                )
                                            )
                                        )
                                        customDialogState.hide()
                                    }, asOrganization = {
                                        onBoardingViewModel.updateUserData(userType = UserType.Organization)
                                        sharedViewModel.goToScreen(
                                            screenNavigator = ScreenNavigator(
                                                screen = Screen.RegistrationScreen,
                                                values = listOf(
                                                    UserType.Organization.type
                                                )
                                            )
                                        )
                                        customDialogState.hide()
                                    })

                                    else -> Unit
                                }
                            }) {
                            ModalBottomSheetLayout(
                                bottomSheetNavigator = bottomSheetNavigator,
                                scrimColor = MaterialTheme.colors.background.copy(alpha = 0.8f)
                            ) {
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
        }
    }


    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
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
                        onBoardingViewModel.onSignIn()
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
                        }, onSignUp = {
                            onBoardingViewModel.onSignUp()
                        }, onAddNewPlace = {
                            mainNavController.navigator(route = BottomSheet.MapSheet.route)
                        })
                    }
            }
            bottomSheet(route = BottomSheet.MapSheet.route){
                DonateTodayMap(modifier = Modifier.fillMaxSize())
            }
        }
    }

    @Composable
    private fun Launchers(
        mainNavController: NavHostController,
        currentScreen: ScreenNavigator?,
        snackBarState: SnackBarState
    ) {
        val loginUIState by onBoardingViewModel.loginUIState.collectAsState()
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
                    route = currentScreen.screen.route.let { route ->
                        if (currentScreen.values.isEmpty())
                            route
                        else
                            route + "/${currentScreen.values.joinToString("/")}"
                    },
                    clearBackStack = clearBackStack,
                    clearBackStackToRoute = clearBackStackToRoute
                )
        }

        LaunchedEffect(key1 = loginUIState) {
            when (loginUIState) {
                is LoginUIState.Success -> sharedViewModel.goToScreen(ScreenNavigator(screen = Screen.OnBoardingScreen))
                    .also {
                        snackBarState.hide()
                    }

                is LoginUIState.Error -> snackBarState.show(
                    overridingText = "Error! ${loginUIState?.message}",
                    overridingDelay = SnackBarLengthLong
                )

                is LoginUIState.Loading -> snackBarState.show(overridingText = "Loading...")
                else -> Unit
            }
        }
    }
}