package ru.d3rvich.core.domain.entities

import kotlinx.datetime.LocalDate

data class GameEntity(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val metacritic: Int?,
    val rating: Float?,
    val ratings: List<RatingEntity>?,
    val released: LocalDate?,
    val genres: List<GenreEntity>?,
    val parentPlatforms: List<ParentPlatformEntity>?,
)