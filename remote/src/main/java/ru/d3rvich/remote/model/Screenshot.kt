package ru.d3rvich.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by Ilya Deryabin at 14.03.2024
 */
@Serializable
data class Screenshot(@SerialName("image") val image: String)