package ru.d3rvich.core.domain.usecases

import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.entity.StoreLinkEntity
import javax.inject.Inject

@Factory
class GetStoreLinksByGameIdUseCase @Inject constructor(private val repository: GamesRepository) {

    suspend operator fun invoke(gameId: Int): Result<List<StoreLinkEntity>> =
        repository.getStoreLinksBy(gameId = gameId)
}