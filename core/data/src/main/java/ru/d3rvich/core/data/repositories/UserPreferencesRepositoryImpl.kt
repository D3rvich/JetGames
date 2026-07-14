package ru.d3rvich.core.data.repositories

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import ru.d3rvich.core.datastore.JetGamesPreferencesDataStore
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.model.ThemeType

@Single(binds = [UserPreferencesRepository::class])
internal class UserPreferencesRepositoryImpl(
    private val dataStore: JetGamesPreferencesDataStore
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> = dataStore.userPreferences

    override fun getListDisplayOption(): Flow<ListDisplayOption> = dataStore.listDisplayOption

    override suspend fun setCurrentTheme(theme: ThemeType) {
        dataStore.setTheme(theme = theme)
    }

    override suspend fun setCurrentColorMode(colorMode: ColorMode) {
        dataStore.setColorMode(colorMode = colorMode)
    }

    override suspend fun setListDisplayOption(option: ListDisplayOption) {
        dataStore.setListDisplayOption(option = option)
    }
}