package ru.d3rvich.core.domain.entities

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
data class PlatformEntity(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val gamesCount: Int,
)
