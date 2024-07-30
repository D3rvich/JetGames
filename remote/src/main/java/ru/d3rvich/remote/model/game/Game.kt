package ru.d3rvich.remote.model.game

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.d3rvich.remote.model.Genre
import ru.d3rvich.remote.model.ParentPlatformWrapper
import ru.d3rvich.remote.model.Rating

/**
 * Created by Ilya Deryabin at 05.02.2024
 */
@Serializable
data class Game(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("background_image") val imageUrl: String?,
    @SerialName("metacritic") val metacritic: Int?,
    @SerialName("rating") val rating: Float?,
    @SerialName("ratings") val ratings : List<Rating>?,
    @SerialName("released") val released: LocalDate?,
    @SerialName("genres") val genres: List<Genre>,
    @SerialName("parent_platforms") val parentPlatforms: List<ParentPlatformWrapper>? = null,
)