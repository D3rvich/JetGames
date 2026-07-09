package ru.d3rvich.core.data.mapper

import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.database.model.GameDBO
import ru.d3rvich.core.database.model.GenreDBO
import ru.d3rvich.core.database.model.ParentPlatformDBO
import ru.d3rvich.core.database.model.PlatformDBO
import ru.d3rvich.core.database.model.RatingDBO
import ru.d3rvich.core.database.model.StoreDBO
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.entity.GenreEntity
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.ParentPlatformEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.entity.RatingEntity
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.entity.StoreEntity
import ru.d3rvich.core.entity.StoreLinkEntity
import ru.d3rvich.core.remote.model.metadata.Genre
import ru.d3rvich.core.remote.model.metadata.GenreFull
import ru.d3rvich.core.remote.model.metadata.ParentPlatform
import ru.d3rvich.core.remote.model.metadata.Platform
import ru.d3rvich.core.remote.model.metadata.Rating
import ru.d3rvich.core.remote.model.details.Screenshot
import ru.d3rvich.core.remote.model.details.Store
import ru.d3rvich.core.remote.model.details.StoreLink
import ru.d3rvich.core.remote.model.game.Game
import ru.d3rvich.core.remote.model.details.GameDetail
import ru.d3rvich.core.remote.result.NetworkResult
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal fun Game.toGameEntity(): GameEntity =
    GameEntity(
        name = name,
        id = id,
        imageUrl = imageUrl,
        metacritic = metacritic,
        rating = rating,
        released = released,
        genres = genres.map { it.toGenreEntity() },
        parentPlatforms = parentPlatforms?.map { it.platform.toParentPlatformEntity() },
        ratings = ratings?.map { it.toRatingEntity() }
    )

internal fun GameDetail.toGameDetailEntity(): GameDetailEntity =
    GameDetailEntity(
        id = id,
        name = name,
        description = description,
        screenshotCount = screenshotCount,
        screenshots = emptyList(),
        released = released,
        metacritic = metacritic,
        imageUrl = imageUrl,
        genres = genres?.map { it.toGenreFullEntity() },
        rating = rating,
        parentPlatforms = parentPlatforms?.map { it.platform.toParentPlatformEntity() },
        ratings = ratings?.map { it.toRatingEntity() },
        stores = stores.map { it.store.toStoreEntity() },
        isFavorite = false,
    )

@OptIn(ExperimentalTime::class)
internal fun GameDetailEntity.toGameDBO(): GameDBO = GameDBO(
    id = id,
    name = name,
    description = description,
    screenshotCount = screenshotCount,
    screenshots = screenshots.map { it.imageUrl },
    released = released,
    metacritic = metacritic,
    imageUrl = imageUrl,
    genres = genres?.map { it.toGenreDBO() },
    rating = rating,
    ratings = ratings?.map { it.toRatingDBO() },
    parentPlatforms = parentPlatforms?.map { it.toParentPlatformDBO() },
    addingTime = Clock.System.now().toEpochMilliseconds(),
    stores = stores.map { it.toStoreBDO() }
)

internal fun GameDBO.toGameDetailEntity(): GameDetailEntity =
    GameDetailEntity(
        id = id,
        name = name,
        description = description,
        screenshotCount = screenshotCount,
        screenshots = screenshots.map { ScreenshotEntity(it) },
        released = released,
        metacritic = metacritic,
        imageUrl = imageUrl,
        genres = genres?.map { it.toGenreFullEntity() },
        rating = rating,
        parentPlatforms = parentPlatforms?.map { it.toParentPlatformEntity() },
        ratings = ratings?.map { it.toRatingEntity() },
        stores = stores.map { it.toStoreEntity() },
        isFavorite = true,
    )

internal fun GameDBO.toGameEntity(): GameEntity =
    GameEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        metacritic = metacritic,
        rating = rating,
        released = released,
        genres = genres?.map { it.toGenreEntity() },
        parentPlatforms = parentPlatforms?.map { it.toParentPlatformEntity() },
        ratings = ratings?.map { it.toRatingEntity() })

internal fun GenreDBO.toGenreFullEntity(): GenreFullEntity =
    GenreFullEntity(id, name, imageUrl, gamesCount)

internal fun Genre.toGenreEntity(): GenreEntity =
    GenreEntity(id, name)

internal fun GenreDBO.toGenreEntity(): GenreEntity =
    GenreEntity(id, name)

internal fun GenreFull.toGenreFullEntity(): GenreFullEntity =
    GenreFullEntity(id, name, imageUrl, gamesCount)

internal fun GenreFullEntity.toGenreDBO(): GenreDBO = GenreDBO(id, name, imageUrl, gamesCount)

internal fun List<Screenshot>.toScreenshotEntityList(): List<ScreenshotEntity> {
    return this.map { ScreenshotEntity(it.image) }
}

internal fun Platform.toPlatformEntity(): PlatformEntity =
    PlatformEntity(id, name, imageUrl, gamesCount)

internal fun PlatformDBO.toPlatformEntity(): PlatformEntity =
    PlatformEntity(id, name, imageUrl, gamesCount)

internal fun PlatformEntity.toPlatformDBO(): PlatformDBO =
    PlatformDBO(id, name, imageUrl, gamesCount)

internal fun ParentPlatform.toParentPlatformEntity(): ParentPlatformEntity =
    ParentPlatformEntity(id, name)

internal fun ParentPlatformDBO.toParentPlatformEntity(): ParentPlatformEntity =
    ParentPlatformEntity(id, name)

internal fun ParentPlatformEntity.toParentPlatformDBO(): ParentPlatformDBO =
    ParentPlatformDBO(id, name)

internal fun Rating.toRatingEntity(): RatingEntity = RatingEntity(id, title, count, percent)

internal fun RatingDBO.toRatingEntity(): RatingEntity = RatingEntity(id, title, count, percent)

internal fun RatingEntity.toRatingDBO(): RatingDBO = RatingDBO(id, title, count, percent)

internal fun Store.toStoreEntity(): StoreEntity = StoreEntity(id, name)

internal fun StoreLink.toGameStoreEntity(): StoreLinkEntity = StoreLinkEntity(id, storeId, url)

internal fun StoreEntity.toStoreBDO(): StoreDBO = StoreDBO(id, name, url)

internal fun StoreDBO.toStoreEntity(): StoreEntity = StoreEntity(id, name, url)

internal fun <T : Any> NetworkResult<T>.asResult(): Result<T> = when (this) {
    is NetworkResult.Failure<*> -> Result.Failure(this.error ?: Exception("Unknown error"))
    is NetworkResult.Success -> Result.Success(this.value)
}
