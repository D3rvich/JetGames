package ru.d3rvich.core.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.model.Result

/**
 * Created by Ilya Deryabin at 04.04.2024
 */
interface GenresRepository {

    fun getGenres(): Flow<Result<List<GenreFullEntity>>>
}