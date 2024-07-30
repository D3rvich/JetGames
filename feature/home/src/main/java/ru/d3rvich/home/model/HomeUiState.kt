package ru.d3rvich.home.model

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.ui.base.UiState
import ru.d3rvich.core.domain.entities.GameEntity

/**
 * Created by Ilya Deryabin at 02.02.2024
 */
@Stable
internal data class HomeUiState(
    val games: Flow<PagingData<GameEntity>>,
    val search: String = "",
    val isFilterEdited: Boolean = false,
) : UiState