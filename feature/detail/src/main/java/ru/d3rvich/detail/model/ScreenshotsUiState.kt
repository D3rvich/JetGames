package ru.d3rvich.detail.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.domain.entities.ScreenshotEntity

/**
 * Created by Ilya Deryabin at 15.03.2024
 */
@Immutable
internal sealed interface ScreenshotsUiState {
    data object Loading : ScreenshotsUiState
    data object NoScreenshots : ScreenshotsUiState
    class Error(val throwable: Throwable) : ScreenshotsUiState
    class Success(val screenshots: List<ScreenshotEntity>) : ScreenshotsUiState
}