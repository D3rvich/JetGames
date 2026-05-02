package ru.d3rvich.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

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