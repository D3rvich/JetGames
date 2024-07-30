package ru.d3rvich.browse.model

import androidx.compose.runtime.Stable
import ru.d3rvich.core.ui.base.UiState
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.entities.GenreFullEntity

@Stable
internal data class BrowseUiState(
    val genres: Status<List<GenreFullEntity>> = Status.Loading,
) : UiState