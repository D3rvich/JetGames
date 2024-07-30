package ru.d3rvich.core.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.repositories.GamesRepository
import ru.d3rvich.core.domain.entities.GameEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 06.04.2024
 */
class GetFavoriteGamesUseCase @Inject constructor(private val gamesRepository: GamesRepository) {
    operator fun invoke(search: String): Flow<PagingData<GameEntity>> =
        gamesRepository.getFavoriteGames(search = search)
}