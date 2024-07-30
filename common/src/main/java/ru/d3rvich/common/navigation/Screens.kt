package ru.d3rvich.common.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.d3rvich.core.domain.model.LoadSource
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
    data class GameDetail(val gameId: Int, val loadSource: LoadSource)

    @Serializable
    data object Filter

    @Serializable
    data class Screenshots(val selectedScreenshot: Int, val screenshots: List<String>)
}

val LoadSource.Companion.NavType: NavType<LoadSource?>
    get() = object : NavType<LoadSource?>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): LoadSource? {
            val value = bundle.getString(key) ?: return null
            return LoadSource.valueOf(value)
        }

        override fun parseValue(value: String): LoadSource {
            return LoadSource.valueOf(value)
        }

        override fun put(bundle: Bundle, key: String, value: LoadSource?) {
            bundle.putString(key, value?.name)
        }
    }

fun NavController.navigateToGameDetailScreen(
    gameId: Int,
    loadSource: LoadSource = LoadSource.Network,
) {
    navigate(
        Screens.GameDetail(gameId = gameId, loadSource = loadSource)
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