package ru.d3rvich.favorites.model

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.ui.base.UiState
import ru.d3rvich.core.domain.entities.GameEntity

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@Stable
internal data class FavoritesUiState(val games: Flow<PagingData<GameEntity>>) : UiState
