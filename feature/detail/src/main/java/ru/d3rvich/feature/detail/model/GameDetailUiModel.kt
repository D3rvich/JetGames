package ru.d3rvich.feature.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.ParentPlatformEntity
import ru.d3rvich.core.entity.RatingEntity
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.entity.StoreEntity

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
    val storesUiModel: StoresUiModel,
    val isFavorite: Boolean,
)

internal sealed class StoresUiModel(val stores: ImmutableList<StoreEntity>) {

    data object Empty : StoresUiModel(persistentListOf())

    class EmptyUrls(stores: ImmutableList<StoreEntity>) : StoresUiModel(stores)

    class Full(stores: ImmutableList<StoreEntity>) : StoresUiModel(stores)
}

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
        storesUiModel = stores.consumeStores(),
        isFavorite = isFavorite
    )

private fun List<StoreEntity>.consumeStores(): StoresUiModel {
    return when {
        isEmpty() -> StoresUiModel.Empty
        any { it.url == null } -> StoresUiModel.EmptyUrls(this.toImmutableList())
        else -> StoresUiModel.Full(this.toImmutableList())
    }
}

internal fun GameDetailUiModel.toGameDetailEntity(): GameDetailEntity =
    GameDetailEntity(
        id = id,
        name = name,
        description = description,
        screenshotCount = screenshotCount,
        screenshots = screenshots,
        released = released,
        metacritic = metacritic,
        imageUrl = imageUrl,
        genres = genres,
        rating = rating,
        ratings = ratings,
        parentPlatforms = parentPlatforms,
        stores = storesUiModel.stores,
        isFavorite = isFavorite
    )