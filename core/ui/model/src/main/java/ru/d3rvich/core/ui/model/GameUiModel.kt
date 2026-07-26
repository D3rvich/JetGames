package ru.d3rvich.core.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.entity.GenreEntity
import ru.d3rvich.core.entity.ParentPlatformEntity
import ru.d3rvich.core.entity.RatingEntity

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

fun GameEntity.toGameUiModel(): GameUiModel = GameUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl,
    metacritic = metacritic,
    rating = rating,
    ratings = ratings?.toImmutableList(),
    released = released,
    genres = genres?.toImmutableList(),
    parentPlatforms = parentPlatforms?.toImmutableList()
)