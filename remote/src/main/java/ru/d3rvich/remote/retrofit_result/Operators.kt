package ru.d3rvich.remote.retrofit_result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by Ilya Deryabin at 09.02.2024
 */
fun <T> NetworkResult<T>.isSuccess(): Boolean {
    return this is NetworkResult.Success
}

fun <T> NetworkResult<T>.asSuccess(): NetworkResult.Success<T> {
    return this as NetworkResult.Success<T>
}

@ExperimentalContracts
fun <T> NetworkResult<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is NetworkResult.Failure<*>)
    }
    return this is NetworkResult.Failure<*>
}

fun <T> NetworkResult<T>.asFailure(): NetworkResult.Failure<*> {
    return this as NetworkResult.Failure<*>
}

fun <T, R> NetworkResult<T>.map(transform: (value: T) -> R): NetworkResult<R> {
    return when(this) {
        is NetworkResult.Success -> NetworkResult.Success.Value(transform(value))
        is NetworkResult.Failure<*> -> this
    }
}

fun <T, R> NetworkResult<T>.flatMap(transform: (result: NetworkResult<T>) -> NetworkResult<R>): NetworkResult<R> {
    return transform(this)
}