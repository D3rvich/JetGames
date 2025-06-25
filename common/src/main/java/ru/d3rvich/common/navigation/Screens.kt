package ru.d3rvich.common.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Created by Ilya Deryabin at 28.02.2024
 */
object Screens {
    @Serializable
    data object Home

    @Serializable
    data object Browse

    @Serializable
    data object Favorites

    @Serializable
    data class GameDetail(val gameId: Int)

    @Serializable
    data object Filter

    @Serializable
    data class Screenshots(val selectedScreenshot: Int, val screenshots: List<String>)

    @Serializable
    data object Settings
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