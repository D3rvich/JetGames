package ru.d3rvich.core.data.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface LocalDataSource<T : Any> {

    suspend fun execute(): T?

    suspend fun update(value: T)
}

internal fun <T : Any> localDataSource(
    execute: suspend () -> T,
    update: suspend (value: T) -> Unit,
): LocalDataSource<T> =
    object : LocalDataSource<T> {

        override suspend fun execute(): T = withContext(Dispatchers.IO) { execute() }

        override suspend fun update(value: T) = withContext(Dispatchers.IO) { update(value) }
    }
