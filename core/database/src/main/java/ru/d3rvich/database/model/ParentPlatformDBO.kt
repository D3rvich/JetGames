package ru.d3rvich.database.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentPlatformDBO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)