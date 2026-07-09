package ru.d3rvich.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.model.Result
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@Factory
class GetGameDetailUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    operator fun invoke(gameId: Int): Flow<Result<GameDetailEntity>> = flow {
        emit(gamesRepository.getGameDetail(gameId))
    }.onStart { emit(Result.Loading) }
}