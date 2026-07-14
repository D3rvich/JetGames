package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.model.ThemeType

interface UserPreferencesRepository {

    fun getUserPreferences(): Flow<UserPreferences>

    fun getListDisplayOption(): Flow<ListDisplayOption>

    suspend fun setCurrentTheme(theme: ThemeType)

    suspend fun setCurrentColorMode(colorMode: ColorMode)

    suspend fun setListDisplayOption(option: ListDisplayOption)
}