package ru.d3rvich.core.domain.usecases

import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.model.Result
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 15.03.2024
 */
@Factory
class GetScreenshotsUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(gameId: Int): Result<List<ScreenshotEntity>> =
        gamesRepository.getGameScreenshots(gameId)
}