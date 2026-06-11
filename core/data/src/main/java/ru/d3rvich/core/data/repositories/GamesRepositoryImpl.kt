package ru.d3rvich.core.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.domain.entities.ScreenshotEntity
import ru.d3rvich.core.domain.entities.StoreLinkEntity
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.map
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.data.mapper.asResult
import ru.d3rvich.core.data.mapper.toGameDBO
import ru.d3rvich.core.data.mapper.toGameDetailEntity
import ru.d3rvich.core.data.mapper.toGameEntity
import ru.d3rvich.core.data.mapper.toGameStoreEntity
import ru.d3rvich.core.data.mapper.toScreenshotEntityList
import ru.d3rvich.core.data.paging.GamesPagingSourceFactory
import ru.d3rvich.core.database.JetGamesDatabase
import ru.d3rvich.core.remote.JetGamesNetworkDataSource
import ru.d3rvich.core.remote.model.details.GameDetail

/**
 * Created by Ilya Deryabin at 01.02.2024
 */
@Single(binds = [GamesRepository::class])
internal class GamesRepositoryImpl(
    private val apiService: JetGamesNetworkDataSource,
    private val database: JetGamesDatabase,
    private val gamesPagingSourceFactory: GamesPagingSourceFactory,
) : GamesRepository {

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    override fun getGames(
        filterPreferencesBody: FilterPreferencesBody,
        search: String,
    ): Flow<PagingData<GameEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                gamesPagingSourceFactory.create(search, filterPreferencesBody)
            }).flow

    override suspend fun getGameDetail(gameId: Int): Result<GameDetailEntity> =
        when (val detail = database.gamesDao.gameDetail(gameId)) {
            null -> {
                apiService.getGameDetail(gameId = gameId).asResult()
                    .map(GameDetail::toGameDetailEntity)
            }

            else -> {
                Result.Success(detail.toGameDetailEntity())
            }
        }

    override suspend fun getGameScreenshots(gameId: Int): Result<List<ScreenshotEntity>> =
        apiService.getScreenshots(gameId = gameId).asResult()
            .map { it.results.toScreenshotEntityList() }

    override suspend fun getStoreLinksBy(gameId: Int): Result<List<StoreLinkEntity>> =
        apiService.getGameStoresById(gameId = gameId).asResult()
            .map { result -> result.results.map { it.toGameStoreEntity() } }

    override fun getFavoriteGames(search: String): Flow<PagingData<GameEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                if (search.isEmpty()) {
                    database.gamesDao.games()
                } else {
                    database.gamesDao.search(query = search)
                }
            }
        ).flow.map { pagingData -> pagingData.map { gameDBO -> gameDBO.toGameEntity() } }

    override suspend fun saveGameDetail(gameDetail: GameDetailEntity): Result<Unit> =
        try {
            database.gamesDao.insert(game = gameDetail.toGameDBO())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override suspend fun deleteGameDetail(gameDetail: GameDetailEntity): Result<Unit> =
        try {
            database.gamesDao.delete(game = gameDetail.toGameDBO())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
}