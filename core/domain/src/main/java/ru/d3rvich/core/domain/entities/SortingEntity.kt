package ru.d3rvich.core.domain.entities

/**
 * Created by Ilya Deryabin at 05.02.2024
 * */
enum class SortingEntity {
    NoSorting,
    Metacritic,
    Rating,
    Name,
    Released
}

fun SortingEntity.getReversed(): String = "-$this"