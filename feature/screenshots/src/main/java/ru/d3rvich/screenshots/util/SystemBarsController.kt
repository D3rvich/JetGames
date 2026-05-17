package ru.d3rvich.screenshots.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ru.d3rvich.core.ui.model.isDarkTheme
import ru.d3rvich.core.ui.settings.LocalUserPreferences
import ru.d3rvich.core.ui.utils.findActivity

@Composable
internal fun SystemBarsController(showSystemBars: Boolean) {
    val context = LocalContext.current
    val window = remember(context) { context.findActivity().window }
    val insetsController =
        remember(window) { WindowCompat.getInsetsController(window, window.decorView) }
    val userPreferences = LocalUserPreferences.current.userPreferences
    val darkTheme = userPreferences.isDarkTheme()
    LaunchedEffect(showSystemBars) {
        insetsController.apply {
            if (!showSystemBars) {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                show(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
}