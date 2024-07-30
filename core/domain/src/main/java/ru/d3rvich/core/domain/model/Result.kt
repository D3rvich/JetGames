package ru.d3rvich.core.domain.model

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
sealed interface Result<out T> {
    class Success<T>(val value: T) : Result<T>
    class Failure(val throwable: Throwable) : Result<Nothing>
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Failure -> this
        is Result.Success -> Result.Success(transform(this.value))
    }
}