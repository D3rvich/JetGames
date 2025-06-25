package ru.d3rvich.core.domain.entities

data class StoreEntity(val id: Int, val name: String, val url: String? = null)

data class GameStoreEntity(val id: Int, val storeId: Int, val url: String)