package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.ColorModeType
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.domain.model.UserPreferences

interface UserPreferencesRepository {

    fun getUserPreferences(): Flow<UserPreferences>

    suspend fun setCurrentTheme(theme: ThemeType)

    suspend fun setCurrentColorMode(colorModeType: ColorModeType)
}