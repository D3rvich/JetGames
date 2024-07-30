package ru.d3rvich.remote.retrofit_result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by Ilya Deryabin at 09.02.2024
 */
fun <T> RetrofitResult<T>.isSuccess(): Boolean {
    return this is RetrofitResult.Success
}

fun <T> RetrofitResult<T>.asSuccess(): RetrofitResult.Success<T> {
    return this as RetrofitResult.Success<T>
}

@ExperimentalContracts
fun <T> RetrofitResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is RetrofitResult.Failure<*>)
    }
    return this is RetrofitResult.Failure<*>
}

fun <T> RetrofitResult<T>.asFailure(): RetrofitResult.Failure<*> {
    return this as RetrofitResult.Failure<*>
}

fun <T, R> RetrofitResult<T>.map(transform: (value: T) -> R): RetrofitResult<R> {
    return when(this) {
        is RetrofitResult.Success -> RetrofitResult.Success.Value(transform(value))
        is RetrofitResult.Failure<*> -> this
    }
}

fun <T, R> RetrofitResult<T>.flatMap(transform: (result: RetrofitResult<T>) -> RetrofitResult<R>): RetrofitResult<R> {
    return transform(this)
}