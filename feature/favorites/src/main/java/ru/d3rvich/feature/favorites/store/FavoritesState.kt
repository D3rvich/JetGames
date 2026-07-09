package ru.d3rvich.feature.favorites.store

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.ui.base.UiState
import ru.d3rvich.core.entity.GameEntity

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@Stable
internal data class FavoritesState(val games: Flow<PagingData<GameEntity>>) : UiState
