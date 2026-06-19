package ru.d3rvich.feature.filter.model

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody

internal data class FilterPreferencesBodyUiModel(
    val sortBy: SortingEntity,
    val isReversed: Boolean,
    val selectedPlatforms: ImmutableSet<PlatformEntity>,
    val selectedGenres: ImmutableSet<GenreFullEntity>,
    val metacriticRange: MetacriticRange,
)

internal fun FilterPreferencesBodyUiModel.isDefault(): Boolean =
    sortBy == SortingEntity.NoSorting && isReversed &&
            selectedPlatforms.isEmpty() && selectedPlatforms.isEmpty() &&
            metacriticRange == MetacriticRange.Unspecific

internal fun FilterPreferencesBody.toFilterPreferencesBodyUiModel(): FilterPreferencesBodyUiModel =
    FilterPreferencesBodyUiModel(
        sortBy = sortBy,
        isReversed = isReversed,
        selectedPlatforms = selectedPlatforms.toImmutableSet(),
        selectedGenres = selectedGenres.toImmutableSet(),
        metacriticRange = metacriticRange
    )

internal fun FilterPreferencesBodyUiModel.toFilterPreferencesBody(): FilterPreferencesBody =
    FilterPreferencesBody(sortBy, isReversed, selectedPlatforms, selectedGenres, metacriticRange)