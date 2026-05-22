package ru.d3rvich.data.repositoties

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.model.ColorModeType
import ru.d3rvich.core.domain.model.ThemeType
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import ru.d3rvich.datastore.JetGamesPreferencesDataStore

@Factory
internal class UserPreferencesRepositoryImpl(
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