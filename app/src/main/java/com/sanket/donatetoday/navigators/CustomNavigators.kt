package com.sanket.donatetoday.navigators

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterialNavigationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberCustomBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        animationSpec = animationSpec
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberCustomAnimatedNavController(vararg navigators: Navigator<out NavDestination>): NavHostController =
    rememberNavController(*navigators).apply {
        navigatorProvider += remember(this) { AnimatedComposeNavigator() }
    }

const val NavigationDuration = 500

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.customAnimatedComposable(
    route: String,
    overridingDuration: Int? = null,
    enterTransitionDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Left,
    exitTransitionDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Left,
    popEnterTransitionDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
    popExitTransitionDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(route, enterTransition = {
    slideIntoContainer(
        enterTransitionDirection,
        animationSpec = tween(overridingDuration?: NavigationDuration)
    )
    /*when (initialState.destination.route) {
        Screen.Home.Tutorial.route ->
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        else -> null
    }*/
},
    exitTransition = {
        slideOutOfContainer(
            exitTransitionDirection,
            animationSpec = tween(overridingDuration?: NavigationDuration)
        )
        /*when (targetState.destination.route) {
            Screen.Home.Tutorial.route ->
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            else -> null
        }*/
    },
    popEnterTransition = {
        slideIntoContainer(
            popEnterTransitionDirection,
            animationSpec = tween(overridingDuration?: NavigationDuration)
        )
        /*when (initialState.destination.route) {
            Screen.Home.Tutorial.route ->
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            else -> null
        }*/
    },
    popExitTransition = {
        slideOutOfContainer(
            popExitTransitionDirection,
            animationSpec = tween(overridingDuration?: NavigationDuration)
        )
        /*when (targetState.destination.route) {
            Screen.Home.Tutorial.route ->
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            else -> null
        }*/
    }) {
    content(it)
}

fun NavController.navigator(route: String, clearBackStack: Boolean = false, clearBackStackToRoute: String? = null) {
    if (clearBackStack && clearBackStackToRoute != null)
        this.popBackStack(clearBackStackToRoute, inclusive = true, false)
    try {
        navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            /*popUpTo(this@navigator.graph.startDestinationId) {
            saveState = true
        }*/

            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    } catch (ex: java.lang.NullPointerException){
        ex.printStackTrace()
    }
}