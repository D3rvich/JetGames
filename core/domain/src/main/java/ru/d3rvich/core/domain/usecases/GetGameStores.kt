package ru.d3rvich.core.domain.usecases

import ru.d3rvich.core.domain.entities.StoreLinkEntity
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.repositories.GamesRepository
import javax.inject.Inject

class GetStoreLinksByGameIdUseCase @Inject constructor(private val repository: GamesRepository) {

    suspend operator fun invoke(gameId: Int): Result<List<StoreLinkEntity>> =
        repository.getStoreLinksBy(gameId = gameId)
}