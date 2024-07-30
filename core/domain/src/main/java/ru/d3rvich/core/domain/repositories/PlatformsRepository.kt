package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.entities.PlatformEntity

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
interface PlatformsRepository {

    fun getPlatforms(): Flow<Status<List<PlatformEntity>>>
}