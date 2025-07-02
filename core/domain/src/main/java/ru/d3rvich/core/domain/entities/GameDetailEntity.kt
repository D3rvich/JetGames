package ru.d3rvich.core.domain.entities

import kotlinx.datetime.LocalDate

data class GameDetailEntity(
    val id: Int,
    val name: String,
    val description: String?,
    val screenshotCount: Int,
    val screenshots: List<ScreenshotEntity>,
    val released: LocalDate?,
    val metacritic: Int?,
    val imageUrl: String?,
    val genres: List<GenreFullEntity>?,
    val rating: Float?,
    val ratings: List<RatingEntity>?,
    val parentPlatforms: List<ParentPlatformEntity>?,
    val stores: List<StoreEntity>,
    val isFavorite: Boolean,
)