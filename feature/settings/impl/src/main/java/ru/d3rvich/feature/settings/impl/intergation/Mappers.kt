package ru.d3rvich.feature.settings.impl.intergation

import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.feature.settings.impl.store.SettingsStore

internal val stateToModel: (SettingsStore.State) -> SettingsComponent.Model = { state ->
    when (state) {
        SettingsStore.State.Loading -> SettingsComponent.Model.Loading
        is SettingsStore.State.Settings -> SettingsComponent.Model.Settings(
            themeType = state.themeType,
            colorMode = state.colorMode,
            inDynamicColorSupported = state.inDynamicColorSupported
        )
    }
}