package ru.d3rvich.core.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.repositories.GenresRepository
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.model.Result
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 04.04.2024
 */
@Factory
class GetGenresUseCase @Inject constructor(private val genresRepository: GenresRepository) {

    operator fun invoke(): Flow<Result<List<GenreFullEntity>>> = genresRepository.getGenres()
}