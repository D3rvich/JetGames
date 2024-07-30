package ru.d3rvich.remote.util

import ru.d3rvich.remote.JetGamesApiService
import ru.d3rvich.remote.model.ApiPagingResult
import ru.d3rvich.remote.model.GenreFull
import ru.d3rvich.remote.model.Platform
import ru.d3rvich.remote.retrofit_result.RetrofitResult

suspend fun JetGamesApiService.getAllPlatforms(): RetrofitResult<List<Platform>> {
    return collectAllPagingSource(apiCall = { page, pageSize -> getPlatforms(page, pageSize) })
}

suspend fun JetGamesApiService.getAllGenres(): RetrofitResult<List<GenreFull>> {
    return collectAllPagingSource { page, pageSize -> getGenres(page, pageSize) }
}

private inline fun <T : Any> collectAllPagingSource(apiCall: (Int, Int) -> RetrofitResult<ApiPagingResult<T>>): RetrofitResult<List<T>> {
    val resultList = mutableListOf<T>()
    var page = 1
    do {
        val maxPageSize = 40
        val result = apiCall(page, maxPageSize)
        when (result) {
            is RetrofitResult.Failure<*> -> {
                return result
            }

            is RetrofitResult.Success -> {
                resultList.addAll(result.value.results)
            }
        }
        page += 1
    } while (result is RetrofitResult.Success && result.value.next != null)
    return RetrofitResult.Success.Value(resultList.toList())
}