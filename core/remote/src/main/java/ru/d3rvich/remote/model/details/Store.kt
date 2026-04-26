package ru.d3rvich.remote.model.details

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class Store(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
@OptIn(InternalSerializationApi::class)
data class StoreWrapper(
    @SerialName("store")
    val store: Store,
    @SerialName("url")
    val url: String
)

@Serializable
@OptIn(InternalSerializationApi::class)
data class StoreLink(
    @SerialName("id")
    val id: Int,
    @SerialName("store_id")
    val storeId: Int,
    @SerialName("url")
    val url: String
)