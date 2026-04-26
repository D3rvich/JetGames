package ru.d3rvich.remote

import ru.d3rvich.remote.model.common.ApiPagingResult
import ru.d3rvich.remote.model.metadata.GenreFull
import ru.d3rvich.remote.model.metadata.Platform
import ru.d3rvich.remote.model.details.Screenshot
import ru.d3rvich.remote.model.details.StoreLink
import ru.d3rvich.remote.model.game.Game
import ru.d3rvich.remote.model.details.GameDetails
import ru.d3rvich.remote.result.NetworkResult

interface JetGamesNetworkDataSource {
    suspend fun getGames(
        page: Int,
        pageSize: Int,
        search: String? = null,
        searchPrecise: Boolean = true,
        sorting: String? = null,
        tags: String? = null,
        platforms: String? = null,
        genres: String? = null,
        metacritic: String? = null,
    ): NetworkResult<ApiPagingResult<Game>>

    suspend fun getGameDetail(gameId: Int): NetworkResult<GameDetails>

    suspend fun getScreenshots(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = 10,
    ): NetworkResult<ApiPagingResult<Screenshot>>

    suspend fun getGameStoresById(
        gameId: Int,
        page: Int = 1,
        pageSize: Int = RAWG_MAX_PAGE_SIZE
    ): NetworkResult<ApiPagingResult<StoreLink>>

    suspend fun getPlatforms(
        page: Int,
        pageSize: Int,
    ): NetworkResult<ApiPagingResult<Platform>>

    suspend fun getGenres(
        page: Int,
        pageSize: Int,
    ): NetworkResult<ApiPagingResult<GenreFull>>
}

private const val RAWG_MAX_PAGE_SIZE = 40