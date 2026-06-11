package ru.d3rvich.core.domain.usecases

import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.entities.GameDetailEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 06.04.2024
 */
@Factory
class RemoveFromFavoritesUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(gameDetail: GameDetailEntity) =
        gamesRepository.deleteGameDetail(gameDetail = gameDetail)
}