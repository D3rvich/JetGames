package ru.d3rvich.core.domain.usecases

import ru.d3rvich.core.domain.entities.GameStoreEntity
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.repositories.StoreRepository
import javax.inject.Inject

class GetGameStoresByIdUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(gameId: Int): Result<List<GameStoreEntity>> =
        repository.getGameStoresById(gameId = gameId)
}