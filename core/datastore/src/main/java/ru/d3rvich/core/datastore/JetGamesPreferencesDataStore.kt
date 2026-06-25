package ru.d3rvich.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import ru.d3rvich.core.domain.model.ListDisplayOption
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType

@Single
class JetGamesPreferencesDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

    val lastSyncPlatformsTimestamp: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[PreferencesScheme.SYNC_TIME_PLATFORMS] ?: DEFAULT_TIMESTAMP
    }

    val lastSyncGenresTimestamp: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[PreferencesScheme.SYNC_TIME_GENRES] ?: DEFAULT_TIMESTAMP
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        val themeRaw = preferences[PreferencesScheme.THEME_TYPE]
        val colorRaw = preferences[PreferencesScheme.COLOR_MODE]
        val theme = if (themeRaw != null) ThemeType.valueOf(themeRaw) else ThemeType.System
        val color = if (colorRaw != null) ColorMode.valueOf(colorRaw) else ColorMode.Default
        UserPreferences(theme, color)
    }

    val listDisplayOption: Flow<ListDisplayOption> = context.dataStore.data.map { preferences ->
        val rawListDisplayOption = preferences[PreferencesScheme.LIST_DISPLAY_OPTION]
        rawListDisplayOption?.let {
            ListDisplayOption.valueOf(rawListDisplayOption)
        } ?: ListDisplayOption.Compact
    }

    suspend fun setTheme(theme: ThemeType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesScheme.THEME_TYPE] = theme.name
        }
    }

    suspend fun setColorMode(colorMode: ColorMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesScheme.COLOR_MODE] = colorMode.name
        }
    }

    suspend fun setLastSyncPlatformsTimestamp(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesScheme.SYNC_TIME_PLATFORMS] = value
        }
    }

    suspend fun setLastSyncGenresTimestamp(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesScheme.SYNC_TIME_GENRES] = value
        }
    }

    suspend fun setListDisplayOption(option: ListDisplayOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesScheme.LIST_DISPLAY_OPTION] = option.toString()
        }
    }
}


private const val DATASTORE_NAME = "user_preferences"

private object PreferencesScheme {
    val THEME_TYPE = stringPreferencesKey("THEME_TYPE")
    val COLOR_MODE = stringPreferencesKey("COLOR_MODE")
    val SYNC_TIME_PLATFORMS = longPreferencesKey("SYNC_PLATFORMS")
    val SYNC_TIME_GENRES = longPreferencesKey("SYNC_GENRES")
    val LIST_DISPLAY_OPTION = stringPreferencesKey("LIST_DISPLAY_OPTION")
}

private const val DEFAULT_TIMESTAMP = -1L