package ru.d3rvich.core.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
sealed interface Status<out T> {
    data object Loading : Status<Nothing>
    class Success<T>(val value: T) : Status<T>
    class Error(val throwable: Throwable) : Status<Nothing>
}

fun <T> Flow<T>.asStatus(): Flow<Status<T>> = map<T, Status<T>> { Status.Success(it) }
    .onStart { emit(Status.Loading) }
    .catch { emit(Status.Error(it)) }