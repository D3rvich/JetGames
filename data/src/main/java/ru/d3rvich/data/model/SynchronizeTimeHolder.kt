package ru.d3rvich.data.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.d3rvich.datastore.JetGamesPreferencesDataStore

internal class SynchronizeTimeHolder(
    private val coroutineScope: CoroutineScope,
    private val dataStore: JetGamesPreferencesDataStore
) {

    var lastSyncPlatformsTimestamp: Long
        get() = dataStore.lastSyncPlatformsTimestamp.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DEFAULT_TIMESTAMP
        ).value
        set(value) {
            coroutineScope.launch { dataStore.setLastSyncPlatformsTimestamp(value) }
        }

    var lastSyncGenresTimestamp: Long
        get() = dataStore.lastSyncGenresTimestamp.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DEFAULT_TIMESTAMP
        ).value
        set(value) {
            coroutineScope.launch { dataStore.setLastSyncGenresTimestamp(value) }
        }

    internal companion object {
        const val DEFAULT_TIMESTAMP = -1L
    }
}