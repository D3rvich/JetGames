package ru.d3rvich.data.model

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.d3rvich.datastore.JetGamesPreferencesDataStore

interface SyncTimeManager {

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

@Factory
@Named("Genres")
internal class GenresSyncTimeManagerImpl(private val dataStore: JetGamesPreferencesDataStore) :
    SyncTimeManager {
    override suspend fun getTimestamp(): Long? = dataStore.lastSyncGenresTimestamp.firstOrNull()

    override suspend fun setTimestamp(value: Long) = dataStore.setLastSyncGenresTimestamp(value)
}

@Factory
@Named("Platforms")
internal class PlatformsSyncTimeManager(private val dataStore: JetGamesPreferencesDataStore) :
    SyncTimeManager {
    override suspend fun getTimestamp(): Long? = dataStore.lastSyncPlatformsTimestamp.firstOrNull()

    override suspend fun setTimestamp(value: Long) = dataStore.setLastSyncPlatformsTimestamp(value)
}

internal enum class SyncField {
    Genres,
    Platforms
}