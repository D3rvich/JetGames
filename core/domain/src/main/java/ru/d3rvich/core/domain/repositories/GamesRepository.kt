package ru.d3rvich.core.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.entity.StoreLinkEntity

interface GamesRepository {
    fun getGames(
        filterPreferencesBody: FilterPreferencesBody,
        search: String = "",
    ): Flow<PagingData<GameEntity>>

    suspend fun getGameDetail(gameId: Int): Result<GameDetailEntity>

    suspend fun getGameScreenshots(gameId: Int): Result<List<ScreenshotEntity>>

    suspend fun getStoreLinksBy(gameId: Int): Result<List<StoreLinkEntity>>

    fun getFavoriteGames(search: String): Flow<PagingData<GameEntity>>

    suspend fun saveGameDetail(gameDetail: GameDetailEntity): Result<Unit>

    suspend fun deleteGameDetail(gameDetail: GameDetailEntity): Result<Unit>
}