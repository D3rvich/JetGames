package ru.d3rvich.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.repositories.PlatformsRepository
import ru.d3rvich.core.domain.entities.PlatformEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
class GetPlatformsUseCase @Inject constructor(private val platformsRepository: PlatformsRepository) {
    operator fun invoke(): Flow<LoadingResult<List<PlatformEntity>>> {
        return platformsRepository.getPlatforms()
    }
}