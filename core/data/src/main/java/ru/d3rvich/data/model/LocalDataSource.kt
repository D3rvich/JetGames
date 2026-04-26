package ru.d3rvich.data.model

internal interface LocalDataSource<T : Any> {

    suspend fun execute(): T?

    suspend fun update(value: T)
}

internal fun <T : Any> localDataSource(
    execute: suspend () -> T,
    update: suspend (value: T) -> Unit,
): LocalDataSource<T> =
    object : LocalDataSource<T> {

        override suspend fun execute(): T = execute()

        override suspend fun update(value: T) = update(value)
    }
