package ru.d3rvich.core.ui.mapper

import kotlinx.collections.immutable.toImmutableList
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.ui.model.GameUiModel

fun GameEntity.toGameUiModel(): GameUiModel = GameUiModel(
    id = id,
    name = name,
    imageUrl = imageUrl,
    metacritic = metacritic,
    rating = rating,
    released = released,
    genres = genres?.toImmutableList(),
    parentPlatforms = parentPlatforms?.toImmutableList(),
    ratings = ratings?.toImmutableList()
)