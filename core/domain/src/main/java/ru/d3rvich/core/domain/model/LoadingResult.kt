package ru.d3rvich.core.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
sealed interface LoadingResult<out T> {
    data object Loading : LoadingResult<Nothing>
    class Success<out T>(val value: T) : LoadingResult<T>
    class Error(val throwable: Throwable) : LoadingResult<Nothing>
}

fun <T> Flow<T>.asLoadingResult(): Flow<LoadingResult<T>> = map<T, LoadingResult<T>> { LoadingResult.Success(it) }
    .onStart { emit(LoadingResult.Loading) }
    .catch { emit(LoadingResult.Error(it)) }