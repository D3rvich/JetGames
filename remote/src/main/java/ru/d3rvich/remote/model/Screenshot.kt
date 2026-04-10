package ru.d3rvich.remote.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 14.03.2024
 */
@Serializable
@OptIn(InternalSerializationApi::class)
data class Screenshot(@SerialName("image") val image: String)