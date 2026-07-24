package ru.d3rvich.core.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.model.FilterPreferencesBody
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
@Factory
class GetGamesUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    operator fun invoke(
        search: String = "",
        filterPrefBody: FilterPreferencesBody,
    ): Flow<PagingData<GameEntity>> {
        return gamesRepository.getGames(search = search, filterPreferencesBody = filterPrefBody)
    }
}