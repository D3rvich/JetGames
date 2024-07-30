package ru.d3rvich.jetgames.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun rememberNavRouter(navController: NavController): NavRouter = remember(navController) {
    NavRouter(navController)
}

class NavRouter(private val navController: NavController) {

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentRoute: String?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route

    fun navigateToDestination(topLevelDestination: TopLevelDestination) {
        navController.navigate(topLevelDestination.route) {
            launchSingleTop = true

            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }

            restoreState = true
        }
    }
}