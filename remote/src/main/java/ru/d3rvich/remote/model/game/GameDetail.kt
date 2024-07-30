package ru.d3rvich.remote.model.game

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.d3rvich.remote.model.GenreFull
import ru.d3rvich.remote.model.ParentPlatformWrapper
import ru.d3rvich.remote.model.Rating

@Serializable
data class GameDetail(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description_raw") val description: String?,
    @SerialName("released") val released: LocalDate?,
    @SerialName("metacritic") val metacritic: Int?,
    @SerialName("background_image") val imageUrl: String?,
    @SerialName("genres") val genres: List<GenreFull>?,
    @SerialName("rating") val rating: Float,
    @SerialName("ratings") val ratings: List<Rating>?,
    @SerialName("screenshots_count") val screenshotCount: Int,
    @SerialName("parent_platforms") val parentPlatforms: List<ParentPlatformWrapper>?,
)