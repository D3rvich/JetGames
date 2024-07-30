package ru.d3rvich.core.domain.entities

data class GenreEntity(
    val id: Int,
    val name: String,
)

data class GenreFullEntity(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val gamesCount: Int,
)
