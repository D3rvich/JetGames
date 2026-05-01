package ru.d3rvich.jetgames.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation.NavController
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ru.d3rvich.browse.BrowseScreen
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.common.navigation.navigateToFilterScreen
import ru.d3rvich.common.navigation.navigateToGameDetailScreen
import ru.d3rvich.common.navigation.navigateToSettingsScreen
import ru.d3rvich.favorites.FavoritesScreen
import ru.d3rvich.home.HomeScreen

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    externalNavController: NavController,
    windowSizeClass: WindowSizeClass,
) {
    val showNavRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val showBottomBar = !showNavRail
    val backStack = rememberNavBackStack(Screens.Favorites)
    val navRouter = remember { NavRouter(backStack) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = if (showNavRail) {
            WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        } else {
            WindowInsets(0)
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navRouter)
            }
        }) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            val disabledAnimationTransitionSpec =
                NavDisplay.transitionSpec { EnterTransition.None togetherWith ExitTransition.None } +
                        NavDisplay.popTransitionSpec { EnterTransition.None togetherWith ExitTransition.None } +
                        NavDisplay.predictivePopTransitionSpec { EnterTransition.None togetherWith ExitTransition.None }
            if (showNavRail) {
                NavRail(navRouter)
            }
            NavDisplay(
                modifier = Modifier.weight(1f),
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<Screens.Home>(metadata = disabledAnimationTransitionSpec) {
                        HomeScreen(
                            contentPadding = paddingValues,
                            navigateToFilterScreen = externalNavController::navigateToFilterScreen,
                            navigateToDetailScreen = externalNavController::navigateToGameDetailScreen,
                            navigateToSettingsScreen = externalNavController::navigateToSettingsScreen
                        )
                    }
                    entry<Screens.Browse>(metadata = disabledAnimationTransitionSpec) {
                        BrowseScreen(contentPadding = paddingValues)
                    }
                    entry<Screens.Favorites>(metadata = disabledAnimationTransitionSpec) {
                        FavoritesScreen(
                            contentPadding = paddingValues,
                            navigateToGameDetail = externalNavController::navigateToGameDetailScreen,
                            navigateToSettingsScreen = externalNavController::navigateToSettingsScreen
                        )
                    }
                })
        }
    }
}

@Composable
private fun NavRail(navRouter: NavRouter, modifier: Modifier = Modifier) {
    NavigationRail(modifier = modifier) {
        navRouter.topLevelDestinations.forEach { destination ->
            val currentRoute = navRouter.currentEntry
            val isSelected = currentRoute == destination.route
            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navRouter.navigateToDestination(destination)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) destination.selectedIconResId
                            else destination.unselectedIconResId
                        ),
                        contentDescription = destination.route.toString()
                    )
                },
                label = { Text(text = stringResource(id = destination.labelResId)) }
            )
        }
    }
}

@Composable
private fun BottomNavBar(navRouter: NavRouter, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier,
        containerColor = NavigationBarDefaults.containerColor.copy(alpha = 0.6f)
    ) {
        navRouter.topLevelDestinations.forEach { destination ->
            val currentRoute = navRouter.currentEntry
            val isSelected = currentRoute == destination.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navRouter.navigateToDestination(destination)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) destination.selectedIconResId
                            else destination.unselectedIconResId
                        ),
                        contentDescription = destination.route.toString()
                    )
                },
                label = { Text(text = stringResource(id = destination.labelResId)) }
            )
        }
    }
}