package com.sanket.donatetoday

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.sanket.donatetoday.delegates.IntentDelegate
import com.sanket.donatetoday.delegates.IntentDelegateImpl
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.modules.common.dialog.CustomDialog
import com.sanket.donatetoday.modules.common.dialog.enums.DialogTypes
import com.sanket.donatetoday.modules.common.dialog.rememberDialogState
import com.sanket.donatetoday.modules.onboarding.LoginScreenMain
import com.sanket.donatetoday.modules.onboarding.RegistrationScreenMain
import com.sanket.donatetoday.modules.onboarding.SignUpOptionDialog
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.modules.common.DonateTodayMonthlyGoalDialog
import com.sanket.donatetoday.modules.common.dialog.CustomDialogState
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.common.loader.DonateTodayLoader
import com.sanket.donatetoday.modules.common.loader.LoaderState
import com.sanket.donatetoday.modules.common.loader.rememberLoaderState
import com.sanket.donatetoday.modules.common.map.DonateTodayMap
import com.sanket.donatetoday.modules.common.snackbar.CustomSnackBar
import com.sanket.donatetoday.modules.common.snackbar.SnackBarLengthLong
import com.sanket.donatetoday.modules.common.snackbar.SnackBarLengthMedium
import com.sanket.donatetoday.modules.common.snackbar.SnackBarState
import com.sanket.donatetoday.modules.common.snackbar.rememberSnackBarState
import com.sanket.donatetoday.modules.donor.DonorDetailScreen
import com.sanket.donatetoday.modules.home.HomeScreenContainer
import com.sanket.donatetoday.modules.home.enums.SettingsEnums
import com.sanket.donatetoday.modules.home.getters.DashboardGetters
import com.sanket.donatetoday.modules.organization.AllOrganizationsList
import com.sanket.donatetoday.modules.organization.CashDonationBottomSheet
import com.sanket.donatetoday.modules.organization.ClothesDonationBottomSheet
import com.sanket.donatetoday.modules.organization.FoodDonationBottomSheet
import com.sanket.donatetoday.modules.organization.OrganizationDetailScreen
import com.sanket.donatetoday.modules.organization.UtensilsDonationBottomSheet
import com.sanket.donatetoday.modules.profile.ProfileScreen
import com.sanket.donatetoday.modules.profile.getters.ProfileScreenGetters
import com.sanket.donatetoday.viewmodel.OnBoardingViewModel
import com.sanket.donatetoday.modules.splash.SplashScreen
import com.sanket.donatetoday.modules.verification.VerificationSheet
import com.sanket.donatetoday.navigators.BottomSheet
import com.sanket.donatetoday.navigators.Screen
import com.sanket.donatetoday.navigators.customAnimatedComposable
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import com.sanket.donatetoday.navigators.navigator
import com.sanket.donatetoday.navigators.rememberCustomAnimatedNavController
import com.sanket.donatetoday.navigators.rememberCustomBottomSheetNavigator
import com.sanket.donatetoday.ui.states.HomeUIState
import com.sanket.donatetoday.ui.states.LoginUIState
import com.sanket.donatetoday.ui.theme.DonateTodayTheme
import com.sanket.donatetoday.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), IntentDelegate by IntentDelegateImpl() {
    private val sharedViewModel by viewModels<SharedViewModel>()
    private val onBoardingViewModel by viewModels<OnBoardingViewModel>()


    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBoardingViewModel.isUserLoggedIn()
        setContent {
            // A surface container using the 'background' color from the theme
            val bottomSheetNavigator = rememberCustomBottomSheetNavigator()
            val mainNavController = rememberCustomAnimatedNavController(bottomSheetNavigator)
            val currentScreen by sharedViewModel.currentScreen.collectAsState()
            val loaderState = rememberLoaderState()
            val customDialogState = rememberDialogState()
            val customSnackBarState = rememberSnackBarState()

            Launchers(
                mainNavController = mainNavController,
                currentScreen = currentScreen,
                snackBarState = customSnackBarState,
                loaderState = loaderState
            )

            DonateTodayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DonateTodayLoader(loaderState = loaderState) {
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
                                            onBoardingViewModel.updateUserData(
                                                userType = UserType.Donor,
                                            )
                                            sharedViewModel.goToScreen(
                                                screenNavigator = ScreenNavigator(
                                                    screen = Screen.RegistrationScreen,
                                                )
                                            )
                                            customDialogState.hide()
                                        }, asOrganization = {
                                            onBoardingViewModel.updateUserData(
                                                userType = UserType.Organization,
                                            )
                                            sharedViewModel.goToScreen(
                                                screenNavigator = ScreenNavigator(
                                                    screen = Screen.RegistrationScreen,
                                                )
                                            )
                                            customDialogState.hide()
                                        })

                                        DialogTypes.MonthlyGoal -> {
                                            val user by sharedViewModel.user.collectAsState()
                                            DonateTodayMonthlyGoalDialog(
                                                totalGoal = user.totalGoal,
                                                onGoalChanged = {
                                                    sharedViewModel.updateUser(
                                                        userDTO = user.copy(
                                                            totalGoal = it
                                                        )
                                                    )
                                                }
                                            )
                                        }

                                        else -> Unit
                                    }
                                }) {
                                ModalBottomSheetLayout(
                                    bottomSheetNavigator = bottomSheetNavigator,
                                    sheetBackgroundColor = MaterialTheme.colors.background,
                                    scrimColor = MaterialTheme.colors.background.copy(alpha = 0.8f)
                                ) {
                                    MainNavHost(
                                        mainNavController = mainNavController,
                                        snackBarState = customSnackBarState,
                                        loaderState = loaderState,
                                        dialogState = customDialogState,
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
    }


    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    @Composable
    private fun MainNavHost(
        mainNavController: NavHostController,
        loaderState: LoaderState,
        snackBarState: SnackBarState,
        dialogState: CustomDialogState,
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
                    },
                    onSignUp = onSignUp,
                )
            }

            customAnimatedComposable(route = Screen.RegistrationScreen.route) {
                val user by onBoardingViewModel.user.collectAsState()
                RegistrationScreenMain(userDTO = user, onBack = {
                    mainNavController.customPopBackStack()
                }, onUpdate = {
                    onBoardingViewModel.updateUserData(userDTO = it)
                }, onSignUp = {
                    onBoardingViewModel.onSignUp()
                }, onAddNewPlace = {
                    mainNavController.navigator(route = BottomSheet.MapSheet.route)
                })
            }

            customAnimatedComposable(route = Screen.HomeScreen.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                val statements by sharedViewModel.filteredListOfAllStatements.collectAsState()
                val organizationCashChartData by sharedViewModel.organizationCashChartData.collectAsState()
                val organizationDonorChartData by sharedViewModel.organizationDonorChartData.collectAsState()
                val donorCashChartData by sharedViewModel.donorCashChartData.collectAsState()
                val recommendedOrganizations by sharedViewModel.listOfRecommended.collectAsState()
                val allOrganizations by sharedViewModel.exploreOrganizations.collectAsState()
                val year by sharedViewModel.year.collectAsState()
                val selectedStatementType by sharedViewModel.selectedStatementType.collectAsState()
                HomeScreenContainer(
                    dashboardGetters = DashboardGetters(
                        userDTO = userDTO,
                        listOfAllStatements = statements,
                        listOfDonationItemUserModel = recommendedOrganizations,
                        organizationCashChartData = organizationCashChartData,
                        organizationDonorChartData = organizationDonorChartData,
                        donorCashChartData = donorCashChartData,
                        allOrganizations = allOrganizations,
                        year = year,
                        selectedStatementTypes = selectedStatementType,
                        onStatementTypeSelected = sharedViewModel::updateSelectedStatementType,
                        onYearChanged = {
                            sharedViewModel.changeYear(it)
                        },
                        onEditMonthlyGoal = {
                            dialogState.show(dialog = DialogTypes.MonthlyGoal)
                        }, onSettingsClick = { settingsEnums ->
                            when (settingsEnums) {
                                SettingsEnums.Profile -> sharedViewModel.goToScreen(
                                    ScreenNavigator(screen = Screen.ProfileScreen)
                                )

                                SettingsEnums.Logout -> mainNavController.logout()
                                else -> Unit
                            }
                        }, onOrganizationClick = {
                            sharedViewModel.goToScreen(
                                ScreenNavigator(
                                    screen = Screen.OrganizationDetail, values = listOf(
                                        it
                                    )
                                )
                            )
                        }, onSearchStatements = {
                            sharedViewModel.filterStatements(search = it)
                        }, onStatementClick = {
                            mainNavController.navigator(route = BottomSheet.UserStatementDetail.route + "/${it}")
                        }, onVerificationRequired = {
                            mainNavController.navigator(route = BottomSheet.VerificationScreen.route)
                        }, onSeeAllOrganizations = {
                            sharedViewModel.goToScreen(ScreenNavigator(screen = Screen.AllOrganizationsList))
                        })
                )
            }
            customAnimatedComposable(route = Screen.OrganizationDetail.route + "/{id}") { navBackStackEntry ->
                navBackStackEntry.arguments?.getString("id")?.let { id ->
                    LaunchedEffect(key1 = id) {
                        sharedViewModel.getOrganizationBasedOnId(id = id)
                    }
                    val homeUIState by sharedViewModel.homeUIState.collectAsState()
                    var organization: UserDTO? by remember(homeUIState) {
                        mutableStateOf(null)
                    }
                    LaunchedEffect(key1 = homeUIState) {
                        when (val state = homeUIState) {
                            is HomeUIState.Loading -> loaderState.show()
                            is HomeUIState.Success -> {
                                organization = state.data
                                loaderState.hide()
                            }

                            else -> snackBarState.show(
                                overridingText = state?.message,
                                overridingDelay = SnackBarLengthMedium
                            ).also { loaderState.hide() }
                        }
                    }
                    OrganizationDetailScreen(organization = organization, onBack = {
                        mainNavController.customPopBackStack()
                    }, onDonateItem = { item ->
                        when (item) {
                            DonationItemTypes.Cash.type -> mainNavController.navigator(route = BottomSheet.DonateCashSheet.route)
                            DonationItemTypes.Clothes.type -> mainNavController.navigator(route = BottomSheet.DonateClothesSheet.route)
                            DonationItemTypes.Utensils.type -> mainNavController.navigator(route = BottomSheet.DonateUtensilsSheet.route)
                            DonationItemTypes.Food.type -> mainNavController.navigator(route = BottomSheet.DonateFoodSheet.route)
                        }

                    }, onEmail = {
                        onEmail(this@MainActivity, it)
                    }, onPhone = {
                        onPhone(this@MainActivity, it)
                    })
                }
            }
            customAnimatedComposable(route = Screen.ProfileScreen.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                ProfileScreen(profileScreenGetters = ProfileScreenGetters(
                    userDTO = userDTO,
                    onBackButton = {
                        mainNavController.customPopBackStack()
                    },
                    onAddNewPlace = {},
                    onUpdateProfile = {
                        sharedViewModel.updateUser(it)
                    }
                ))
            }
            customAnimatedComposable(route = Screen.AllOrganizationsList.route) {
                val allFilteredOrganizations by sharedViewModel.allFilteredOrganizations.collectAsState()
                val selectedDonationTypes by sharedViewModel.selectedDonationItemTypes.collectAsState()
                AllOrganizationsList(
                    allFilteredOrganizations = allFilteredOrganizations,
                    selectedDonationTypes = selectedDonationTypes,
                    onSearchOrganizations = sharedViewModel::filterOrganizations,
                    onDonationTypeSelected = sharedViewModel::selectDonationItemTypes,
                    onClick = {
                        sharedViewModel.goToScreen(
                            ScreenNavigator(
                                screen = Screen.OrganizationDetail, values = listOf(
                                    it.id
                                )
                            )
                        )
                    },
                    onPhone = {
                        onPhone(this@MainActivity, it)
                    }, onEmail = {
                        onEmail(this@MainActivity, it)
                    }
                )
            }
            bottomSheet(route = BottomSheet.MapSheet.route) {
                DonateTodayMap(modifier = Modifier.fillMaxSize())
            }
            bottomSheet(route = BottomSheet.DonateCashSheet.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                CashDonationBottomSheet(userDTO = userDTO, onDonate = {
                    mainNavController.popBackStack()
                    sharedViewModel.addCashDonation(amount = it)
                }, onGoToProfile = {
                    mainNavController.popBackStack()
                    sharedViewModel.goToScreen(screenNavigator = ScreenNavigator(screen = Screen.ProfileScreen))
                })
            }
            bottomSheet(route = BottomSheet.DonateClothesSheet.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                ClothesDonationBottomSheet(userDTO = userDTO, onDonate = {
                    mainNavController.popBackStack()
                    sharedViewModel.addClothesDonation(genericDonationData = it)
                })
            }
            bottomSheet(route = BottomSheet.DonateUtensilsSheet.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                UtensilsDonationBottomSheet(userDTO = userDTO, onDonate = {
                    mainNavController.popBackStack()
                    sharedViewModel.addUtensilsDonation(genericDonationData = it)
                })
            }
            bottomSheet(route = BottomSheet.DonateFoodSheet.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                FoodDonationBottomSheet(userDTO = userDTO, onDonate = {
                    mainNavController.popBackStack()
                    sharedViewModel.addFoodDonation(genericDonationData = it)
                })
            }
            bottomSheet(route = BottomSheet.UserStatementDetail.route + "/{id}") { navBackStackEntry ->
                navBackStackEntry.arguments?.getString("id")?.let { id ->
                    LaunchedEffect(key1 = id) {
                        sharedViewModel.getUserBasedOnId(id = id)
                    }
                    val homeUIState by sharedViewModel.homeUIState.collectAsState()
                    var user: UserDTO? by remember(homeUIState) {
                        mutableStateOf(null)
                    }
                    val callPermissionState =
                        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
                            user?.let {
                                onPhone(this@MainActivity, it)
                            }
                        }
                    LaunchedEffect(key1 = homeUIState) {
                        when (val state = homeUIState) {
                            is HomeUIState.Loading -> loaderState.show()
                            is HomeUIState.Success -> {
                                user = state.data
                                loaderState.hide()
                            }

                            else -> snackBarState.show(
                                overridingText = state?.message,
                                overridingDelay = SnackBarLengthMedium
                            ).also { loaderState.hide() }
                        }
                    }
                    DonorDetailScreen(user = user, onMessage = {

                    }, onEmail = {
                        onEmail(activity = this@MainActivity, userDTO = it)
                    }, onPhone = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                android.Manifest.permission.CALL_PHONE
                            ) -> onPhone(this@MainActivity, it)

                            else -> callPermissionState.launch(android.Manifest.permission.CALL_PHONE)
                        }
                    })
                }
            }

            bottomSheet(route = BottomSheet.VerificationScreen.route) {
                val userDTO by sharedViewModel.user.collectAsState()
                VerificationSheet(userDTO = userDTO, onVerify = {
                    onBoardingViewModel.sendEmailVerification()
                    mainNavController.popBackStack()
                    snackBarState.show("Verification sent", overridingDelay = SnackBarLengthMedium)
                })
            }
        }
    }

    @Composable
    private fun Launchers(
        mainNavController: NavHostController,
        currentScreen: ScreenNavigator?,
        snackBarState: SnackBarState,
        loaderState: LoaderState
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
                is LoginUIState.Success -> sharedViewModel.goToScreen(ScreenNavigator(screen = Screen.HomeScreen))
                    .also {
                        sharedViewModel.getUser(email = onBoardingViewModel.user.value.emailAddress)
                        sharedViewModel.getUserFromFirebaseAsynchronously(userDTO = onBoardingViewModel.user.value)
                        loaderState.hide()
                        snackBarState.show(
                            overridingText = loginUIState?.message,
                            overridingDelay = SnackBarLengthMedium
                        )
                    }

                is LoginUIState.Error -> snackBarState.show(
                    overridingText = "Error! ${loginUIState?.message}",
                    overridingDelay = SnackBarLengthLong
                ).also {
                    loaderState.hide()
                }

                is LoginUIState.Loading -> loaderState.show()
                else -> Unit
            }
        }
    }

    private fun NavController.customPopBackStack() {
        sharedViewModel.goToScreen(screenNavigator = null)
        popBackStack()
    }

    private fun NavController.logout() {
        FirebaseAuth.getInstance().signOut()
        onBoardingViewModel.clearData()
        sharedViewModel.clearData()
        navigator(route = Screen.OnBoardingScreen.route, clearBackStack = true)
    }
}