package ru.d3rvich.core.ui.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.domain.model.UserPreferences

@Immutable
data class UserPreferencesUiState(val userPreferences: UserPreferences)

fun UserPreferences.asUiState(): UserPreferencesUiState = UserPreferencesUiState(this)

@Composable
fun UserPreferences.isDarkTheme(): Boolean = when (theme) {
    ThemeType.Light -> false
    ThemeType.Dark -> true
    ThemeType.System -> isSystemInDarkTheme()
}