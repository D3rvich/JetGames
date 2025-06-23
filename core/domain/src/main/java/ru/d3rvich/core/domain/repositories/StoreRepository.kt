package ru.d3rvich.core.domain.repositories

import ru.d3rvich.core.domain.entities.GameStoreEntity
import ru.d3rvich.core.domain.entities.StoreEntity
import ru.d3rvich.core.domain.model.Result

interface StoreRepository {

    suspend fun getAllStores(): Result<List<StoreEntity>>

    suspend fun getGameStoresById(gameId: Int): Result<List<GameStoreEntity>>
}