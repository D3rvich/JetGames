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
    private val _currentTheme = MutableStateFlow(DefaultTheme)
    val currentTheme = _currentTheme.asStateFlow()

    val isDarkTheme: Boolean
        @Composable
        get() = when(currentTheme.collectAsState().value) {
            ThemeType.Light -> false
            ThemeType.Dark -> true
            ThemeType.SystemDefault -> isSystemInDarkTheme()
        }

    fun setCurrentTheme(themeType: ThemeType) {
        _currentTheme.value = themeType
    }

    private val _useDynamicColor =
        MutableStateFlow(if (!isDynamicColorSupported()) DynamicColorType.NotSupported else DynamicColorType.Selected)
    val useDynamicColor = _useDynamicColor.asStateFlow()

    fun setDynamicColor(dynamicColorsType: DynamicColorType) {
        if (!isDynamicColorSupported()) {
            _useDynamicColor.value = DynamicColorType.NotSupported
        } else {
            _useDynamicColor.value = dynamicColorsType
        }
    }
}

private val DefaultTheme = ThemeType.SystemDefault

@Immutable
enum class ThemeType(@StringRes val titleResId: Int) {
    Light(R.string.theme_light),
    Dark(R.string.theme_dark),
    SystemDefault(R.string.theme_system)
}

@Immutable
enum class DynamicColorType {
    NotSupported,
    Selected,
    Unselected
}

private fun isDynamicColorSupported(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S