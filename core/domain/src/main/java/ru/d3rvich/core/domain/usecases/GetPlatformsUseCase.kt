package ru.d3rvich.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.repositories.PlatformsRepository
import ru.d3rvich.core.entity.PlatformEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
@Factory
class GetPlatformsUseCase @Inject constructor(private val platformsRepository: PlatformsRepository) {
    operator fun invoke(): Flow<LoadingResult<List<PlatformEntity>>> {
        return platformsRepository.getPlatforms()
    }
}