package ru.d3rvich.core.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.LoadSource
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.domain.entities.ScreenshotEntity

interface GamesRepository {
    fun getGames(
        search: String = "",
        filterPreferencesBody: FilterPreferencesBody,
    ): Flow<PagingData<GameEntity>>

    suspend fun getGameDetail(gameId: Int, loadSource: LoadSource): Result<GameDetailEntity>

    suspend fun getGameScreenshots(gameId: Int): Result<List<ScreenshotEntity>>

    fun getFavoriteGames(search: String): Flow<PagingData<GameEntity>>

    suspend fun isGameFavorite(gameId: Int): Boolean

    suspend fun saveGameDetail(gameDetail: GameDetailEntity): Result<Unit>

    suspend fun deleteGameDetail(gameDetail: GameDetailEntity): Result<Unit>
}