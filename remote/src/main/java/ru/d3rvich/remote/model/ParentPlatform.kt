package ru.d3rvich.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentPlatform(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)

@Serializable
data class ParentPlatformWrapper(
    @SerialName("platform")
    val platform: ParentPlatform,
)