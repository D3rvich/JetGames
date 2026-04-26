package ru.d3rvich.remote.util

import ru.d3rvich.remote.JetGamesNetworkDataSource
import ru.d3rvich.remote.model.common.ApiPagingResult
import ru.d3rvich.remote.model.metadata.GenreFull
import ru.d3rvich.remote.model.metadata.Platform
import ru.d3rvich.remote.result.NetworkResult

suspend fun JetGamesNetworkDataSource.getAllPlatforms(): NetworkResult<List<Platform>> =
    collectAllPagingSource { page, pageSize -> getPlatforms(page, pageSize) }

suspend fun JetGamesNetworkDataSource.getAllGenres(): NetworkResult<List<GenreFull>> =
    collectAllPagingSource { page, pageSize -> getGenres(page, pageSize) }

private inline fun <T : Any> collectAllPagingSource(block: (Int, Int) -> NetworkResult<ApiPagingResult<T>>): NetworkResult<List<T>> {
    val resultList = mutableListOf<T>()
    var page = 1
    do {
        val maxPageSize = 40
        val result = block(page, maxPageSize)
        when (result) {
            is NetworkResult.Failure<*> -> {
                return result
            }

            is NetworkResult.Success -> {
                resultList.addAll(result.value.results)
            }
        }
        page += 1
    } while (result.value.next != null)
    return NetworkResult.Success(resultList.toList())
}