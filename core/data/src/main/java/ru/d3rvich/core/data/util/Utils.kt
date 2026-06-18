package ru.d3rvich.core.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.asLoadingResult
import ru.d3rvich.core.data.model.LocalDataSource
import ru.d3rvich.core.data.model.SyncTimeManager
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class, ExperimentalAtomicApi::class)
internal fun <T : Any> cashedRemoteRequest(
    syncTimeManager: SyncTimeManager,
    localDataSource: LocalDataSource<T>,
    remoteCall: suspend () -> Result<T>,
    minDaysToSync: Int = MIN_SYNC_DAYS,
): Flow<LoadingResult<T>> = flow {
    require(minDaysToSync >= 0) { "minDaysToSync is expected to be greater or equal 0" }
    val duration = syncTimeManager.getTimestamp()?.let { lastSyncNotNull ->
        Clock.System.now() - Instant.fromEpochMilliseconds(lastSyncNotNull)
    }
    val isDataEmitted = AtomicBoolean(false)
    localDataSource.execute()?.also { localData ->
        emit(localData)
        isDataEmitted.store(true)
    }
    if (duration != null && duration.inWholeDays >= minDaysToSync) {
        withContext(Dispatchers.IO) {
            remoteCall().also { result ->
                when (result) {
                    is Result.Success -> {
                        localDataSource.update(result.value)
                        syncTimeManager.setTimestamp(Clock.System.now().toEpochMilliseconds())
                        emit(result.value)
                    }

                    is Result.Failure -> {
                        if (!isDataEmitted.load()) {
                            throw result.throwable
                        }
                    }
                }
            }
        }
    }
}.asLoadingResult()

private const val MIN_SYNC_DAYS = 7