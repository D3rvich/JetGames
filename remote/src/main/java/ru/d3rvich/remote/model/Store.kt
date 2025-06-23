package ru.d3rvich.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Store(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class StoreWrapper(
    @SerialName("store")
    val store: Store,
    @SerialName("url")
    val url: String
)

@Serializable
data class GameStore(
    @SerialName("id")
    val id: Int,
    @SerialName("store_id")
    val storeId: Int,
    @SerialName("url")
    val url: String
)