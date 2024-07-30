package ru.d3rvich.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 02.04.2024
 */
@Serializable
data class Platform(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image_background") val imageUrl: String?,
    @SerialName("games_count") val gamesCount: Int,
)
