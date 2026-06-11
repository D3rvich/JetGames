package ru.d3rvich.feature.detail.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.ui.base.UiState

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@Immutable
internal sealed interface GameDetailUiState : UiState {
    data object Loading : GameDetailUiState
    data class Detail(
        val gameDetail: GameDetailUiModel,
        val screenshots: ScreenshotsUiState,
        val stores: StoresUiState
    ) : GameDetailUiState

    class Error(val errorMessage: String) : GameDetailUiState
}