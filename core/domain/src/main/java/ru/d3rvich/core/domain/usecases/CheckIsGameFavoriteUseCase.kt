package ru.d3rvich.core.domain.usecases

import ru.d3rvich.core.domain.repositories.GamesRepository
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 06.04.2024
 */
class CheckIsGameFavoriteUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(gameId: Int): Boolean =
        gamesRepository.isGameFavorite(gameId = gameId)
}