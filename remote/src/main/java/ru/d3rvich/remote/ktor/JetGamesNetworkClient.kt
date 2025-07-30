package ru.d3rvich.remote.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.serialization.InternalSerializationApi
import ru.d3rvich.remote.JetGamesNetworkDataSource
import ru.d3rvich.remote.model.ApiPagingResult
import ru.d3rvich.remote.model.GenreFull
import ru.d3rvich.remote.model.Platform
import ru.d3rvich.remote.model.Screenshot
import ru.d3rvich.remote.model.StoreLink
import ru.d3rvich.remote.model.game.Game
import ru.d3rvich.remote.model.game.GameDetail
import ru.d3rvich.remote.retrofit_result.HttpException
import ru.d3rvich.remote.retrofit_result.NetworkResult

@InternalSerializationApi
internal class JetGamesNetworkClient(private val client: HttpClient) : JetGamesNetworkDataSource {

    override suspend fun getGames(
        page: Int,
        pageSize: Int,
        search: String?,
        searchPrecise: Boolean,
        sorting: String?,
        tags: String?,
        platforms: String?,
        genres: String?,
        metacritic: String?
    ): NetworkResult<ApiPagingResult<Game>> = client.performCall(
        Routes.Games(
            page = page,
            pageSize = pageSize,
            search = search,
            searchPrecise = searchPrecise,
            sorting = sorting,
            tags = tags,
            platforms = platforms,
            genres = genres,
            metacritic = metacritic
        )
    )

    override suspend fun getGameDetail(gameId: Int): NetworkResult<GameDetail> =
        client.performCall(Routes.Games.Detail(gameId = gameId))

    override suspend fun getScreenshots(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<Screenshot>> =
        client.performCall(Routes.Games.Detail.Screenshots(gameId = gameId))

    override suspend fun getGameStoresById(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<StoreLink>> = client.performCall(
        Routes.Games.Detail.Stores(gameId = gameId)
    )

    override suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<Platform>> =
        client.performCall(Routes.Platforms(page = page, pageSize = pageSize))

    override suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<GenreFull>> =
        client.performCall(Routes.Genres(page = page, pageSize = pageSize))
}

private suspend inline fun <reified T : Any, reified R : Any> HttpClient.performCall(resource: T): NetworkResult<R> {
    return try {
        val response = get(resource)
        when (val statusCode = response.status.value) {
            in 200..299 -> {
                NetworkResult.Success.HttpResponse(response.body<R>(), statusCode)
            }

            in 400..599 -> {
                NetworkResult.Failure.HttpError(HttpException(statusCode))
            }

            else -> NetworkResult.Failure.Error(RuntimeException())
        }
    } catch (exception: Exception) {
        NetworkResult.Failure.Error(exception)
    }
}