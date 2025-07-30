package ru.d3rvich.remote.util

import ru.d3rvich.remote.JetGamesNetworkDataSource
import ru.d3rvich.remote.model.ApiPagingResult
import ru.d3rvich.remote.model.GenreFull
import ru.d3rvich.remote.model.Platform
import ru.d3rvich.remote.retrofit_result.NetworkResult

suspend fun JetGamesNetworkDataSource.getAllPlatforms(): NetworkResult<List<Platform>> {
    return collectAllPagingSource(apiCall = { page, pageSize -> getPlatforms(page, pageSize) })
}

suspend fun JetGamesNetworkDataSource.getAllGenres(): NetworkResult<List<GenreFull>> {
    return collectAllPagingSource { page, pageSize -> getGenres(page, pageSize) }
}

private inline fun <T : Any> collectAllPagingSource(apiCall: (Int, Int) -> NetworkResult<ApiPagingResult<T>>): NetworkResult<List<T>> {
    val resultList = mutableListOf<T>()
    var page = 1
    do {
        val maxPageSize = 40
        val result = apiCall(page, maxPageSize)
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
    return NetworkResult.Success.Value(resultList.toList())
}