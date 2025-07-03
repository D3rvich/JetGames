package ru.d3rvich.data.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.model.asStatus
import ru.d3rvich.data.model.SynchronizeTimeHolder
import ru.d3rvich.remote.retrofit_result.RetrofitResult
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal inline fun <T : Any> cashedRemoteRequest(
    crossinline syncTimeProvider: () -> Long,
    crossinline syncTimeSaver: (Long) -> Unit,
    localDataSource: LocalDataSource<T>,
    crossinline remoteCall: suspend () -> Result<T>,
): Flow<Status<T>> = flow {
    val currentTime = Clock.System.now().toEpochMilliseconds()
    if (syncTimeProvider() != SynchronizeTimeHolder.DEFAULT_TIMESTAMP) {
        val diff = currentTime - syncTimeProvider()
        val diffInDays = diff / 1000 / 60 / 60 / 24
        val minSyncTimeInDays = 7
        if (localDataSource.checkIsDataExist()) {
            emit(localDataSource.execute())
        }
        if (diffInDays >= minSyncTimeInDays) {
            when (val result = remoteCall()) {
                is Result.Success -> {
                    localDataSource.update(result.value)
                    syncTimeSaver(Clock.System.now().toEpochMilliseconds())
                    emit(result.value)
                }

                is Result.Failure -> {
                    if (!localDataSource.checkIsDataExist()) {
                        throw result.throwable
                    }
                }
            }
        }
    } else {
        when (val result = remoteCall()) {
            is Result.Success -> {
                localDataSource.update(result.value)
                syncTimeSaver(Clock.System.now().toEpochMilliseconds())
                emit(result.value)
            }

            is Result.Failure -> {
                throw result.throwable
            }
        }
    }
}.asStatus()

internal interface LocalDataSource<T : Any> {
    suspend fun checkIsDataExist(): Boolean

    suspend fun execute(): T

    suspend fun update(value: T)
}

internal inline fun <T : Any> LocalDataSource(
    crossinline execute: suspend () -> T,
    crossinline update: suspend (value: T) -> Unit,
    crossinline checkIsDataExist: suspend () -> Boolean = { false },
): LocalDataSource<T> =
    object : LocalDataSource<T> {
        override suspend fun checkIsDataExist(): Boolean = checkIsDataExist()

        override suspend fun execute(): T = execute()

        override suspend fun update(value: T) = update(value)
    }

internal suspend fun <T : Any> safeApiCall(
    isErrorExpected: Boolean = true,
    call: suspend () -> RetrofitResult<T>,
): Result<T> {
    return when (val result = call()) {
        is RetrofitResult.Failure<*> -> {
            if (isErrorExpected) {
                delay(DelayTimeMillis)
                safeApiCall(false, call)
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