package ru.d3rvich.data.model

import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Qualifier
import ru.d3rvich.datastore.JetGamesPreferencesDataStore

interface SyncTimeManager {

    suspend fun getTimestamp(): Long?

    suspend fun setTimestamp(value: Long)
}

@Factory(binds = [SyncTimeManager::class])
@GenresSync
internal class GenresSyncTimeManager(private val dataStore: JetGamesPreferencesDataStore) :
    SyncTimeManager {
    override suspend fun getTimestamp(): Long? = dataStore.lastSyncGenresTimestamp.firstOrNull()

    override suspend fun setTimestamp(value: Long) = dataStore.setLastSyncGenresTimestamp(value)
}

@Factory(binds = [SyncTimeManager::class])
@PlatformsSync
internal class PlatformsSyncTimeManager(private val dataStore: JetGamesPreferencesDataStore) :
    SyncTimeManager {
    override suspend fun getTimestamp(): Long? = dataStore.lastSyncPlatformsTimestamp.firstOrNull()

    override suspend fun setTimestamp(value: Long) = dataStore.setLastSyncPlatformsTimestamp(value)
}

@Qualifier
annotation class GenresSync

@Qualifier
annotation class PlatformsSync