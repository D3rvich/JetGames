package ru.d3rvich.core.navigation.settings

import ru.d3rvich.feature.settings.SettingsViewModel

interface SettingsComponent {
    val settingsViewModel: SettingsViewModel

    fun onBackClick()
}