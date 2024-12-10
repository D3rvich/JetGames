package ru.d3rvich.browse.model

import androidx.compose.runtime.Stable
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.ui.base.UiState

@Stable
internal data class BrowseUiState(
    val genres: Status<List<GenreFullEntity>> = Status.Loading,
    val platforms: Status<List<PlatformEntity>> = Status.Loading
) : UiState