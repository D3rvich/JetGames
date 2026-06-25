package ru.d3rvich.core.domain.model

import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType

data class UserPreferences(
    val theme: ThemeType = ThemeType.System,
    val colorMode: ColorMode = ColorMode.Default
)
