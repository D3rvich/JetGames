package ru.d3rvich.data.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.data.model.SynchronizeTimeHolder
import ru.d3rvich.core.domain.model.asLoadingResult
import ru.d3rvich.data.model.LocalDataSource
import ru.d3rvich.remote.JetGamesApiService
import ru.d3rvich.remote.retrofit_result.RetrofitResult
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal fun <T : Any> cashedRemoteRequest(
    syncTimeProvider: suspend () -> Long?,
    syncTimeSaver: suspend (Long) -> Unit,
    localDataSource: LocalDataSource<T>,
    remoteCall: suspend () -> Result<T>,
    minDaysToSync: Int = MIN_SYNC_DAYS,
): Flow<LoadingResult<T>> = flow {
    require(minDaysToSync >= 0) { "minDaysToSync is expected to be greater or equal 0" }
    val duration = syncTimeProvider()?.let { lastSyncNotNull ->
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
                    syncTimeSaver(Clock.System.now().toEpochMilliseconds())
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

internal suspend fun <T : Any> JetGamesApiService.repeatableCall(
    repeatTimes: Int = 1,
    call: suspend JetGamesApiService.() -> RetrofitResult<T>,
): Result<T> {
    require(repeatTimes >= 0) { "repeatTimes is expected to be greater or equal 0" }
    return when (val result = call()) {
        is RetrofitResult.Failure<*> -> {
            if (repeatTimes > 0) {
                delay(DelayTimeMillis)
                repeatableCall(repeatTimes - 1, call)
            } else {
                Result.Failure(result.error ?: Exception("Unknown error"))
            }
        }

        is RetrofitResult.Success -> {
            Result.Success(result.value)
        }
    }
}

private const val DelayTimeMillis = 5000L
private const val MIN_SYNC_DAYS = 7