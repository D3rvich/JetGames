package ru.d3rvich.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.core.domain.repositories.ColorModeType
import ru.d3rvich.core.domain.repositories.SettingsData
import ru.d3rvich.core.domain.repositories.ThemeType

class JetGamesPreferencesDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

    val lastSyncPlatformsTimestamp: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[PreferencesScheme.SYNC_TIME_PLATFORMS] ?: DEFAULT_TIMESTAMP
    }

    val lastSyncGenresTimestamp: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[PreferencesScheme.SYNC_TIME_GENRES] ?: DEFAULT_TIMESTAMP
    }

    val settingsData: Flow<SettingsData?> = context.dataStore.data.map { preferences ->
        val themeRaw = preferences[PreferencesScheme.THEME_TYPE]
        val colorRaw = preferences[PreferencesScheme.COLOR_MODE]
        val theme = if (themeRaw != null) ThemeType.valueOf(themeRaw) else ThemeType.System
        val color = if (colorRaw != null) ColorModeType.valueOf(colorRaw) else ColorModeType.Default
        SettingsData(theme, color)
    }

    suspend fun setTheme(theme: ThemeType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesScheme.THEME_TYPE] = theme.name
        }
    }

    suspend fun setColorMode(colorMode: ColorModeType) {
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
}


private const val DATASTORE_NAME = "settings"

private object PreferencesScheme {
    val THEME_TYPE = stringPreferencesKey("THEME_TYPE")
    val COLOR_MODE = stringPreferencesKey("COLOR_MODE")
    val SYNC_TIME_PLATFORMS = longPreferencesKey("SYNC_PLATFORMS")
    val SYNC_TIME_GENRES = longPreferencesKey("SYNC_GENRES")
}

private const val DEFAULT_TIMESTAMP = -1L