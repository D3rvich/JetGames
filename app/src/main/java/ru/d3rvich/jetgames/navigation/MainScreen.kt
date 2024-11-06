package ru.d3rvich.jetgames.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.d3rvich.browse.BrowseScreen
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.common.navigation.navigateToFilterScreen
import ru.d3rvich.common.navigation.navigateToGameDetailScreen
import ru.d3rvich.core.domain.model.LoadSource
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
    startDestination: Any = Screens.Favorites,
) {
    val showNavRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
    val showBottomBar = !showNavRail
    val navController = rememberNavController()
    val navState = rememberNavRouter(navController = navController)
    Scaffold(modifier = modifier.fillMaxSize(),
        contentWindowInsets = if (showNavRail) {
            WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        } else {
            WindowInsets(0)
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navRouter = navState)
            }
        }) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
        ) {
            if (showNavRail) {
                NavRail(navRouter = navState)
            }
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.weight(1f)
            ) {
                composable<Screens.Home> {
                    HomeScreen(
                        contentPadding = paddingValues,
                        navigateToFilterScreen = { externalNavController.navigateToFilterScreen() },
                        navigateToDetailScreen = { gameId ->
                            externalNavController.navigateToGameDetailScreen(gameId)
                        }
                    )
                }
                composable<Screens.Browse> {
                    BrowseScreen(contentPadding = paddingValues)
                }
                composable<Screens.Favorites> {
                    FavoritesScreen(
                        contentPadding = paddingValues
                    ) { gameId ->
                        externalNavController.navigateToGameDetailScreen(
                            gameId = gameId,
                            loadSource = LoadSource.Local
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavRail(modifier: Modifier = Modifier, navRouter: NavRouter) {
    NavigationRail(modifier = modifier) {
        navRouter.topLevelDestinations.forEach { destination ->
            val currentRoute = navRouter.currentRoute
            val isSelected = currentRoute == destination.route::class.qualifiedName
            NavigationRailItem(selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navRouter.navigateToDestination(destination)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        },
                        contentDescription = destination.route.toString()
                    )
                },
                label = { Text(text = stringResource(id = destination.labelResId)) }
            )
        }
    }
}

@Composable
private fun BottomNavBar(modifier: Modifier = Modifier, navRouter: NavRouter) {
    NavigationBar(
        modifier = modifier,
        containerColor = NavigationBarDefaults.containerColor.copy(alpha = 0.6f)
    ) {
        navRouter.topLevelDestinations.forEach { destination ->
            val currentRoute = navRouter.currentRoute
            val isSelected = currentRoute == destination.route::class.qualifiedName
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navRouter.navigateToDestination(destination)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        },
                        contentDescription = destination.route.toString()
                    )
                },
                label = { Text(text = stringResource(id = destination.labelResId)) }
            )
        }
    }
}