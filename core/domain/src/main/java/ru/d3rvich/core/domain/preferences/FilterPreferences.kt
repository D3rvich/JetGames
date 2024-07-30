package ru.d3rvich.core.domain.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ilya Deryabin at 07.03.2024
 */
@Singleton
class FilterPreferences @Inject constructor() {

    private val _filterPreferencesFlow = MutableStateFlow(FilterPreferencesBody.default())

    val filterPreferencesFlow: StateFlow<FilterPreferencesBody>
        get() = _filterPreferencesFlow.asStateFlow()

    fun applyFilterPreferences(body: FilterPreferencesBody) {
        _filterPreferencesFlow.value = body
    }
}

data class FilterPreferencesBody(
    val sortBy: SortingEntity,
    val isReversed: Boolean,
    val selectedPlatforms: List<PlatformEntity>,
    val selectedGenres: List<GenreFullEntity>,
    val metacriticRange: MetacriticRange,
) {

    override fun equals(other: Any?): Boolean {
        return if (other !is FilterPreferencesBody) {
            false
        } else {
            other.sortBy == sortBy
                    && other.isReversed == isReversed
                    && other.selectedPlatforms == selectedPlatforms
                    && other.selectedGenres == selectedGenres
                    && other.metacriticRange == metacriticRange
        }
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
            selectedPlatforms = emptyList(),
            selectedGenres = emptyList(),
            metacriticRange = MetacriticRange.None
        )
    }
}

fun FilterPreferencesBody.isDefault(): Boolean = this == FilterPreferencesBody.default()