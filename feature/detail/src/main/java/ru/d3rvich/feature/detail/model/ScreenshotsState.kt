package ru.d3rvich.feature.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.ScreenshotEntity

/**
 * Created by Ilya Deryabin at 15.03.2024
 */
@Immutable
internal sealed interface ScreenshotsState {
    data object Loading : ScreenshotsState
    data object NoScreenshots : ScreenshotsState
    data class Error(val throwable: Throwable) : ScreenshotsState
    data class Success(val screenshots: ImmutableList<ScreenshotEntity>) : ScreenshotsState
}