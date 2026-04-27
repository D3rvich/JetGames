package ru.d3rvich.common.navigation

import androidx.navigation.NavController
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Created by Ilya Deryabin at 28.02.2024
 */
object Screens {
    @Serializable
    data object Home: NavKey

    @Serializable
    data object Browse: NavKey

    @Serializable
    data object Favorites: NavKey

    @Serializable
    data class GameDetail(val gameId: Int): NavKey

    @Serializable
    data object Filter: NavKey

    @Serializable
    data class Screenshots(val selectedScreenshot: Int, val screenshots: List<String>): NavKey

    @Serializable
    data object Settings: NavKey
}

fun NavController.navigateToGameDetailScreen(gameId: Int) {
    navigate(
        Screens.GameDetail(gameId = gameId)
    )
}

fun NavController.navigateToFilterScreen() {
    navigate(Screens.Filter)
}

fun NavController.navigateToScreenshotsScreen(selectedScreenshot: Int, screenshots: List<String>) {
    navigate(
        Screens.Screenshots(
            selectedScreenshot = selectedScreenshot,
            screenshots = screenshots.map { URLEncoder.encode(it, StandardCharsets.UTF_8.name()) })
    )
}

fun NavController.navigateToSettingsScreen() {
    navigate(Screens.Settings)
}