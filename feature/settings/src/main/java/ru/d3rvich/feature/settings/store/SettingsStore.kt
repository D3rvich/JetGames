package ru.d3rvich.feature.settings.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType

internal interface SettingsStore : Store<SettingsStore.Intent, SettingsStore.State, Nothing> {
    @Immutable
    sealed interface State {
        data object Loading : State

        data class Settings(
            val themeType: ThemeType,
            val colorMode: ColorMode,
            val inDynamicColorSupported: Boolean
        ) : State
    }

    sealed interface Intent {
        data class ThemeTypeSelected(val themeType: ThemeType) : Intent

        data class ColorModeSelected(val colorMode: ColorMode) : Intent
    }
}