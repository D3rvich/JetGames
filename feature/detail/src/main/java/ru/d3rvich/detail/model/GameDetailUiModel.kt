package ru.d3rvich.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.ParentPlatformEntity
import ru.d3rvich.core.domain.entities.RatingEntity
import ru.d3rvich.core.domain.entities.ScreenshotEntity
import ru.d3rvich.core.domain.entities.StoreEntity

/**
 * Created by Ilya Deryabin at 25.06.2024
 */
@Immutable
internal data class GameDetailUiModel(
    val id: Int,
    val name: String,
    val description: String?,
    val screenshotCount: Int,
    val screenshots: ImmutableList<ScreenshotEntity>,
    val released: LocalDate?,
    val metacritic: Int?,
    val imageUrl: String?,
    val genres: ImmutableList<GenreFullEntity>?,
    val rating: Float?,
    val ratings: ImmutableList<RatingEntity>?,
    val parentPlatforms: ImmutableList<ParentPlatformEntity>?,
    val stores: ImmutableList<StoreEntity>?,
    val isFavorite: Boolean,
)

internal fun GameDetailEntity.toGameDetailUiModel(): GameDetailUiModel =
    GameDetailUiModel(
        id = id,
        name = name,
        description = description,
        screenshotCount = screenshotCount,
        screenshots = screenshots.toImmutableList(),
        released = released,
        metacritic = metacritic,
        imageUrl = imageUrl,
        genres = genres?.toImmutableList(),
        rating = rating,
        parentPlatforms = parentPlatforms?.toImmutableList(),
        ratings = ratings?.toImmutableList(),
        stores = stores.toImmutableList(),
        isFavorite = isFavorite
    )