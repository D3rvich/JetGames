package ru.d3rvich.core.domain.entities

data class RatingEntity(
    val id: Int,
    val title: String,
    val count: Int,
    val percent: Float,
)