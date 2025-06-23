package ru.d3rvich.data.repositoties

import ru.d3rvich.core.domain.entities.GameStoreEntity
import ru.d3rvich.core.domain.entities.StoreEntity
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.map
import ru.d3rvich.core.domain.repositories.StoreRepository
import ru.d3rvich.data.mapper.toGameStoreEntity
import ru.d3rvich.data.util.safeApiCall
import ru.d3rvich.remote.JetGamesApiService

internal class StoreRepositoryImpl(private val apiService: JetGamesApiService) : StoreRepository {

    override suspend fun getGameStoresById(gameId: Int): Result<List<GameStoreEntity>> =
        safeApiCall {
            apiService.getGameStoresById(gameId)
        }.map { result -> result.results.map { it.toGameStoreEntity() } }

    override suspend fun getAllStores(): Result<List<StoreEntity>> {
        TODO("Not yet implemented")
    }
}