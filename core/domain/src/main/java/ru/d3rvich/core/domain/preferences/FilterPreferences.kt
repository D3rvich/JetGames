package ru.d3rvich.core.domain.preferences

import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Single
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.model.MetacriticRange
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 07.03.2024
 */
@Single
class FilterPreferences @Inject constructor() {
    val filterPreferencesFlow: StateFlow<FilterPreferencesBody>
        field = MutableStateFlow(FilterPreferencesBody.default())

    fun applyFilterPreferences(body: FilterPreferencesBody) {
        filterPreferencesFlow.value = body
    }

    fun reset() {
        filterPreferencesFlow.value = FilterPreferencesBody.default()
    }
}

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
            selectedPlatforms = persistentSetOf(),
            selectedGenres = persistentSetOf(),
            metacriticRange = MetacriticRange.None
        )
    }
}

fun FilterPreferencesBody.isDefault(): Boolean = this == FilterPreferencesBody.default()