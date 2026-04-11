package ru.d3rvich.remote.ktor

import io.ktor.client.HttpClient
import kotlinx.serialization.InternalSerializationApi
import ru.d3rvich.remote.JetGamesNetworkDataSource
import ru.d3rvich.remote.model.common.ApiPagingResult
import ru.d3rvich.remote.model.metadata.GenreFull
import ru.d3rvich.remote.model.metadata.Platform
import ru.d3rvich.remote.model.details.Screenshot
import ru.d3rvich.remote.model.details.StoreLink
import ru.d3rvich.remote.model.game.Game
import ru.d3rvich.remote.model.details.GameDetails
import ru.d3rvich.remote.result.NetworkResult
import ru.d3rvich.remote.result.safeApiCall

/**
 * Api documentation [here](https://api.rawg.io/docs/)
 * */
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
    ): NetworkResult<ApiPagingResult<Game>> = client.safeApiCall {
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
    }

    override suspend fun getGameDetail(gameId: Int): NetworkResult<GameDetails> =
        client.safeApiCall { Routes.Games.Detail(gameId = gameId) }

    override suspend fun getScreenshots(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<Screenshot>> = client.safeApiCall {
        val detail = Routes.Games.Detail(gameId = gameId)
        Routes.Games.Detail.Screenshots(detail)
    }

    override suspend fun getGameStoresById(
        gameId: Int,
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<StoreLink>> = client.safeApiCall {
        val detail = Routes.Games.Detail(gameId = gameId)
        Routes.Games.Detail.Stores(detail)
    }

    override suspend fun getPlatforms(
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<Platform>> = client.safeApiCall {
        Routes.Platforms(page = page, pageSize = pageSize)
    }

    override suspend fun getGenres(
        page: Int,
        pageSize: Int
    ): NetworkResult<ApiPagingResult<GenreFull>> = client.safeApiCall {
        Routes.Genres(page = page, pageSize = pageSize)
    }
}