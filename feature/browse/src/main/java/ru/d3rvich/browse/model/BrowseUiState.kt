package ru.d3rvich.browse.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.ui.base.UiState

@Immutable
internal data class BrowseUiState(
    val genres: LoadingResult<List<GenreFullEntity>> = LoadingResult.Loading,
    val platforms: LoadingResult<List<PlatformEntity>> = LoadingResult.Loading
) : UiState