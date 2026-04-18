package ru.d3rvich.core.domain.model

data class UserPreferences(
    val theme: ThemeType = ThemeType.System,
    val colorMode: ColorModeType = ColorModeType.Default
)

enum class ThemeType {
    Light,
    Dark,
    System
}

enum class ColorModeType {
    Default,
    Dynamic
}
