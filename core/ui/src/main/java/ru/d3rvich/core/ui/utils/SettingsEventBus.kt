package ru.d3rvich.core.ui.utils

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.d3rvich.core.ui.R

/**
 * Created by Ilya Deryabin at 05.09.2024
 */
@Stable
object SettingsEventBus {
    val isDynamicColorSupported by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    private val _currentTheme = MutableStateFlow(DefaultTheme)
    val currentTheme = _currentTheme.asStateFlow()

    val isDarkTheme: Boolean
        @Composable
        get() = when (currentTheme.collectAsState().value) {
            ThemeType.Light -> false
            ThemeType.Dark -> true
            ThemeType.System -> isSystemInDarkTheme()
        }

    fun setCurrentTheme(themeType: ThemeType) {
        _currentTheme.value = themeType
    }

    private val _colorMode =
        MutableStateFlow(if (!isDynamicColorSupported) ColorModeType.Default else ColorModeType.Dynamic)
    val colorMode = _colorMode.asStateFlow()

    fun setColorMode(dynamicColorsType: ColorModeType) {
        if (!isDynamicColorSupported) {
            _colorMode.value = ColorModeType.Default
        } else {
            _colorMode.value = dynamicColorsType
        }
    }
}

private val DefaultTheme = ThemeType.System

@Immutable
enum class ThemeType(@StringRes val titleResId: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    System(R.string.theme_system)
}

@Immutable
enum class ColorModeType(@StringRes val titleResId: Int) {
    Default(R.string.color_mode_default),
    Dynamic(R.string.color_mode_dynamic)
}