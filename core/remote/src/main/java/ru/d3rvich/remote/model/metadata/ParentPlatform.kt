package ru.d3rvich.remote.model.metadata

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class ParentPlatform(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)

@Serializable
@OptIn(InternalSerializationApi::class)
data class ParentPlatformWrapper(
    @SerialName("platform")
    val platform: ParentPlatform,
)