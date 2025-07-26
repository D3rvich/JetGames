package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.entities.GenreFullEntity

/**
 * Created by Ilya Deryabin at 04.04.2024
 */
interface GenresRepository {

    fun getGenres(): Flow<LoadingResult<List<GenreFullEntity>>>
}