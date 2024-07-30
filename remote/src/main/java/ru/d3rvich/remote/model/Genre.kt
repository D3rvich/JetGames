package ru.d3rvich.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 12.03.2024
 */
@Serializable
data class Genre(@SerialName("id") val id: Int, @SerialName("name") val name: String)

@Serializable
data class GenreFull(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("image_background") val imageUrl: String?,
    @SerialName("games_count") val gamesCount: Int,
)