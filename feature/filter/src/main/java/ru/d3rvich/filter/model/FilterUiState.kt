package ru.d3rvich.filter.model

import ru.d3rvich.core.ui.base.UiState
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
internal data class FilterUiState(
    val sortingList: List<SortingEntity>,
    val platforms: List<PlatformEntity>,
    val genres: List<GenreFullEntity>,
    val filterPreferencesBody: FilterPreferencesBody,
) : UiState