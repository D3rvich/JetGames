package ru.d3rvich.core.ui.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.domain.model.UserPreferences

@Immutable
data class UserPreferencesUiState(val userPreferences: UserPreferences)

fun UserPreferences.asUiState(): UserPreferencesUiState = UserPreferencesUiState(this)