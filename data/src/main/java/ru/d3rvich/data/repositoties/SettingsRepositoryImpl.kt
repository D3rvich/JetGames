package ru.d3rvich.data.repositoties

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.core.domain.repositories.ColorModeType
import ru.d3rvich.core.domain.repositories.SettingsData
import ru.d3rvich.core.domain.repositories.SettingsRepository
import ru.d3rvich.core.domain.repositories.ThemeType
import ru.d3rvich.datastore.JetGamesPreferencesDataStore
import javax.inject.Inject

internal class SettingsRepositoryImpl @Inject constructor(private val dataStore: JetGamesPreferencesDataStore) :
    SettingsRepository {

    override fun getSettingsData(): Flow<SettingsData> =
        dataStore.settingsData.map { settingsData ->
            settingsData ?: SettingsData(ThemeType.System, ColorModeType.Default)
        }

    override suspend fun setCurrentTheme(theme: ThemeType) {
        dataStore.setTheme(theme = theme)
    }

    override suspend fun setCurrentColorMode(colorModeType: ColorModeType) {
        dataStore.setColorMode(colorMode = colorModeType)
    }
}