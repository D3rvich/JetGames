package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettingsData(): Flow<SettingsData>

    suspend fun setCurrentTheme(theme: ThemeType)

    suspend fun setCurrentColorMode(colorModeType: ColorModeType)
}

data class SettingsData(val theme: ThemeType, val colorMode: ColorModeType)

enum class ThemeType {
    Light,
    Dark,
    System
}

enum class ColorModeType {
    Default,
    Dynamic
}