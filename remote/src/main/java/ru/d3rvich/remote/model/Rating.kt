package ru.d3rvich.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 27.06.2024
 */
@Serializable
data class Rating(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("count") val count: Int,
    @SerialName("percent") val percent: Float,
)
