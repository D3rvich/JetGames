package ru.d3rvich.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ru.d3rvich.core.domain.model.ColorModeType
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.ui.model.UserPreferencesUiState
import ru.d3rvich.core.ui.model.asUiState
import ru.d3rvich.core.ui.settings.LocalUserPreferences
import ru.d3rvich.core.ui.utils.findActivity

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color.Black,
    surfaceVariant = Color.DarkGray,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun JetGamesTheme(
    userPreferencesUiState: UserPreferencesUiState = UserPreferences().asUiState(),
    content: @Composable () -> Unit,
) {
    val userPreferences =
        remember(userPreferencesUiState) { userPreferencesUiState.userPreferences }
    val dynamicColor =
        remember(userPreferences) { userPreferences.colorMode == ColorModeType.Dynamic }
    val darkTheme = when (userPreferences.theme) {
        ThemeType.Light -> false
        ThemeType.Dark -> true
        ThemeType.System -> isSystemInDarkTheme()
    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = view.context.findActivity().window
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                @Suppress("DEPRECATION")
                window.statusBarColor = android.graphics.Color.TRANSPARENT
            }
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}