package ru.d3rvich.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.asLoadingResult
import ru.d3rvich.data.model.LocalDataSource
import ru.d3rvich.data.model.SyncTimeManager
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
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
    var isDataEmitted = false
    localDataSource.execute()?.also { localData ->
        emit(localData)
        isDataEmitted = true
    }
    if (duration != null && duration.inWholeDays >= minDaysToSync) {
        remoteCall().also { result ->
            when (result) {
                is Result.Success -> {
                    localDataSource.update(result.value)
                    syncTimeManager.setTimestamp(Clock.System.now().toEpochMilliseconds())
                    emit(result.value)
                }

                is Result.Failure -> {
                    if (!isDataEmitted) {
                        throw result.throwable
                    }
                }
            }
        }
    }
}.asLoadingResult()

private const val MIN_SYNC_DAYS = 7