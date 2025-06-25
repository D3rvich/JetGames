package ru.d3rvich.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.model.asStatus
import ru.d3rvich.core.domain.repositories.GamesRepository
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
class GetGameDetailUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    operator fun invoke(gameId: Int): Flow<Status<GameDetailEntity>> = flow {
        when (val result = gamesRepository.getGameDetail(gameId = gameId)) {
            is Result.Success -> {
                emit(result.value)
            }

            is Result.Failure -> {
                throw result.throwable
            }
        }
    }.asStatus()
}