package ru.d3rvich.data.repositoties

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.ColorModeType
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import ru.d3rvich.datastore.JetGamesPreferencesDataStore
import javax.inject.Inject

internal class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: JetGamesPreferencesDataStore
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> = dataStore.userPreferences

    override suspend fun setCurrentTheme(theme: ThemeType) {
        dataStore.setTheme(theme = theme)
    }

    override suspend fun setCurrentColorMode(colorModeType: ColorModeType) {
        dataStore.setColorMode(colorMode = colorModeType)
    }
}