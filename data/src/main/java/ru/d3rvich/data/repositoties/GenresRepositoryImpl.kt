package ru.d3rvich.data.repositoties

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.map
import ru.d3rvich.core.domain.repositories.GenresRepository
import ru.d3rvich.data.mapper.toGenreDBO
import ru.d3rvich.data.mapper.toGenreFullEntity
import ru.d3rvich.data.mapper.asResult
import ru.d3rvich.data.model.SynchronizeTimeHolder
import ru.d3rvich.data.model.localDataSource
import ru.d3rvich.data.util.cashedRemoteRequest
import ru.d3rvich.database.JetGamesDatabase
import ru.d3rvich.remote.JetGamesApiService
import ru.d3rvich.remote.util.getAllGenres

/**
 * Created by Ilya Deryabin at 04.04.2024
 */
internal class GenresRepositoryImpl(
    private val apiService: JetGamesApiService,
    private val database: JetGamesDatabase,
    private val syncTimeHolder: SynchronizeTimeHolder,
) : GenresRepository {

    override fun getGenres(): Flow<LoadingResult<List<GenreFullEntity>>> {
        val localDataSource = localDataSource(
            execute = { database.genresDao.genres().map { it.toGenreFullEntity() } },
            update = { genres -> database.genresDao.insert(genres.map { it.toGenreDBO() }) })
        return cashedRemoteRequest(
            syncTimeProvider = { syncTimeHolder.lastSyncGenresTimestamp },
            syncTimeSaver = { syncTimeHolder.lastSyncGenresTimestamp = it },
            localDataSource = localDataSource,
            remoteCall = {
                apiService.getAllGenres().asResult().map { list -> list.map { it.toGenreFullEntity() } }
            }
        )
    }

}