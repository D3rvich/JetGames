package ru.d3rvich.core.entity

data class RatingEntity(
    val id: Int,
    val title: String,
    val count: Int,
    val percent: Float,
)