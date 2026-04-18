package ru.d3rvich.core.ui.settings

import androidx.compose.runtime.staticCompositionLocalOf
import ru.d3rvich.core.ui.model.UserPreferencesUiState

val LocalUserPreferences = staticCompositionLocalOf<UserPreferencesUiState> {
    error("UserPreferences not provided!")
}