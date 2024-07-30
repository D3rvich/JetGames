package ru.d3rvich.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.repositories.GenresRepository
import ru.d3rvich.core.domain.entities.GenreFullEntity
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 04.04.2024
 */
class GetGenresUseCase @Inject constructor(private val genresRepository: GenresRepository) {

    operator fun invoke(): Flow<Status<List<GenreFullEntity>>> = genresRepository.getGenres()
}