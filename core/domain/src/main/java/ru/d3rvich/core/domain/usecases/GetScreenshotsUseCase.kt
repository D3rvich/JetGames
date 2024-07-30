package ru.d3rvich.core.domain.usecases

import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.entities.ScreenshotEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 15.03.2024
 */
class GetScreenshotsUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(gameId: Int): Result<List<ScreenshotEntity>> =
        gamesRepository.getGameScreenshots(gameId)
}