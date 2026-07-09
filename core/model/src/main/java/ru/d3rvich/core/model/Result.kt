package ru.d3rvich.core.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data object Loading : Result<Nothing>
    data class Success<out T>(val value: T) : Result<T>
    data class Error(val throwable: Throwable) : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> = map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Loading) }
    .catch { emit(Result.Error(it)) }

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    Result.Loading -> Result.Loading
    is Result.Error -> this
    is Result.Success -> Result.Success(transform(this.value))
}