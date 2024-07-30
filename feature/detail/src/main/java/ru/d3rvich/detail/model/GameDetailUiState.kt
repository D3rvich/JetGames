package ru.d3rvich.detail.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.ui.base.UiState
import ru.d3rvich.core.domain.entities.GameDetailEntity

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@Immutable
internal sealed interface GameDetailUiState : UiState {
    data object Loading : GameDetailUiState
    data class Detail(
        val gameDetail: GameDetailEntity,
        val screenshots: ScreenshotsUiState,
        val isFavorite: Boolean,
    ) : GameDetailUiState

    class Error(val errorMessage: String) : GameDetailUiState
}