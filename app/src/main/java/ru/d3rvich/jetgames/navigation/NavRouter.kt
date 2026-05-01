package ru.d3rvich.jetgames.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

@Stable
class NavRouter(private val backStack: NavBackStack<NavKey>) {
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentEntry: NavKey
        get() = backStack.last()

    fun navigateToDestination(topLevelDestination: TopLevelDestination) {
        backStack.add(topLevelDestination.route)
    }
}