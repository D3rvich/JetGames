package ru.d3rvich.core.data.repositories

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import ru.d3rvich.core.data.mapper.asResult
import ru.d3rvich.core.data.mapper.toPlatformDBO
import ru.d3rvich.core.data.mapper.toPlatformEntity
import ru.d3rvich.core.data.model.PlatformsSync
import ru.d3rvich.core.data.model.SyncTimeManager
import ru.d3rvich.core.data.model.localDataSource
import ru.d3rvich.core.data.util.cashedRemoteRequest
import ru.d3rvich.core.database.JetGamesDatabase
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.map
import ru.d3rvich.core.domain.repositories.PlatformsRepository
import ru.d3rvich.core.remote.JetGamesNetworkDataSource
import ru.d3rvich.core.remote.util.getAllPlatforms

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
@Single(binds = [PlatformsRepository::class])
internal class PlatformsRepositoryImpl(
    private val apiService: JetGamesNetworkDataSource,
    private val database: JetGamesDatabase,
    @param:PlatformsSync private val syncTimeManager: SyncTimeManager,
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