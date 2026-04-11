package ru.d3rvich.remote.model.common

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 25.03.2024
 */
@Serializable
@OptIn(InternalSerializationApi::class)
data class ApiPagingResult<T>(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String?,
    @SerialName("previous") val previous: String?,
    @SerialName("results") val results: List<T>,
)