package ru.d3rvich.data.model

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import ru.d3rvich.datastore.JetGamesPreferencesDataStore

internal interface SyncTimeManager {

    suspend fun getTimestamp(): Long?

    suspend fun setTimestamp(value: Long)
}

internal class SyncTimeManagerImpl @AssistedInject constructor(
    private val dataStore: JetGamesPreferencesDataStore,
    @Assisted private val field: SyncField,
) : SyncTimeManager {
    override suspend fun getTimestamp(): Long? = when (field) {
        SyncField.Genres -> dataStore.lastSyncGenresTimestamp.firstOrNull()
        SyncField.Platforms -> dataStore.lastSyncPlatformsTimestamp.firstOrNull()
    }

    override suspend fun setTimestamp(value: Long) = when (field) {
        SyncField.Genres -> dataStore.setLastSyncGenresTimestamp(value)
        SyncField.Platforms -> dataStore.setLastSyncPlatformsTimestamp(value)
    }

    @AssistedFactory
    internal interface Factory {
        fun create(field: SyncField): SyncTimeManagerImpl
    }

}

internal enum class SyncField {
    Genres,
    Platforms
}