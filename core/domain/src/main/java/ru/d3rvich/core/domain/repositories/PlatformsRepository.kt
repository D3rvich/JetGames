package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.model.Result

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
interface PlatformsRepository {

    fun getPlatforms(): Flow<Result<List<PlatformEntity>>>
}