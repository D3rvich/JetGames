package ru.d3rvich.core.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate
import ru.d3rvich.core.domain.entities.GenreEntity
import ru.d3rvich.core.domain.entities.ParentPlatformEntity
import ru.d3rvich.core.domain.entities.RatingEntity

/**
 * Created by Ilya Deryabin at 24.06.2024
 */
@Immutable
data class GameUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val metacritic: Int?,
    val rating: Float?,
    val ratings: ImmutableList<RatingEntity>?,
    val released: LocalDate?,
    val genres: ImmutableList<GenreEntity>?,
    val parentPlatforms: ImmutableList<ParentPlatformEntity>?
)