package ru.d3rvich.core.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.entities.GameEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
class GetGamesUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    operator fun invoke(
        search: String = "",
        filterPrefBody: FilterPreferencesBody,
    ): Flow<PagingData<GameEntity>> {
        return gamesRepository.getGames(search = search, filterPreferencesBody = filterPrefBody)
    }
}