package ru.d3rvich.core.domain.usecases

import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.entities.GameDetailEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 06.04.2024
 */
class RemoveFromFavoritesUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(gameDetail: GameDetailEntity) =
        gamesRepository.deleteGameDetail(gameDetail = gameDetail)
}