package ru.d3rvich.core.model

import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.entity.SortingEntity

data class FilterPreferencesBody(
    val sortBy: SortingEntity,
    val isReversed: Boolean,
    val selectedPlatforms: Set<PlatformEntity>,
    val selectedGenres: Set<GenreFullEntity>,
    val metacriticRange: MetacriticRange,
) {

    override fun equals(other: Any?): Boolean {
        if (other !is FilterPreferencesBody)
            return false
        return other.sortBy == sortBy
                && other.isReversed == isReversed
                && other.selectedPlatforms == selectedPlatforms
                && other.selectedGenres == selectedGenres
                && other.metacriticRange == metacriticRange
    }

    override fun hashCode(): Int {
        var result = sortBy.hashCode()
        result = 31 * result + isReversed.hashCode()
        result = 31 * result + selectedPlatforms.hashCode()
        result = 31 * result + selectedGenres.hashCode()
        result = 31 * result + metacriticRange.hashCode()
        return result
    }

    companion object {
        fun default(): FilterPreferencesBody = FilterPreferencesBody(
            sortBy = SortingEntity.NoSorting,
            isReversed = true,
            selectedPlatforms = setOf(),
            selectedGenres = setOf(),
            metacriticRange = MetacriticRange.Unspecific
        )
    }
}

fun FilterPreferencesBody.isDefault(): Boolean = this == FilterPreferencesBody.default()