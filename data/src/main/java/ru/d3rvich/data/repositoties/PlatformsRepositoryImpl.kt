package ru.d3rvich.data.repositoties

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.map
import ru.d3rvich.core.domain.repositories.PlatformsRepository
import ru.d3rvich.data.mapper.asResult
import ru.d3rvich.data.mapper.toPlatformDBO
import ru.d3rvich.data.mapper.toPlatformEntity
import ru.d3rvich.data.model.SyncTimeManager
import ru.d3rvich.data.model.localDataSource
import ru.d3rvich.data.util.cashedRemoteRequest
import ru.d3rvich.database.JetGamesDatabase
import ru.d3rvich.remote.JetGamesApiService
import ru.d3rvich.remote.util.getAllPlatforms

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
internal class PlatformsRepositoryImpl(
    private val apiService: JetGamesApiService,
    private val database: JetGamesDatabase,
    private val syncTimeManager: SyncTimeManager,
) : PlatformsRepository {

    override fun getPlatforms(): Flow<LoadingResult<List<PlatformEntity>>> {
        val localDataSource = localDataSource(
            execute = { database.platformsDao.platforms().map { it.toPlatformEntity() } },
            update = { platforms ->
                database.platformsDao.insert(platforms.map { it.toPlatformDBO() })
            })
        return cashedRemoteRequest(
            syncTimeManager = syncTimeManager,
            localDataSource = localDataSource,
            remoteCall = {
                apiService.getAllPlatforms().asResult()
                    .map { list -> list.map { it.toPlatformEntity() } }
            },
        )
    }
}