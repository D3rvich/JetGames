package ru.d3rvich.screenshots.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import ru.d3rvich.screenshots.model.DragToDismissState
import ru.d3rvich.screenshots.model.draggableScreenshot
import kotlin.math.roundToInt

/**
 * Created by Ilya Deryabin at 27.05.2024
 */
@Composable
internal fun ScreenshotView(
    modifier: Modifier = Modifier,
    screenshot: String,
    pageOffset: Float,
    dragToDismissState: DragToDismissState,
    windowSizeClass: WindowSizeClass,
    onHeightOffsetChange: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AsyncImage(
        model = screenshot,
        contentDescription = null,
        contentScale = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
            ContentScale.FillWidth
        } else {
            ContentScale.FillHeight
        },
        alignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
                alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
            .offset {
                IntOffset(0, dragToDismissState.heightOffset.roundToInt())
            }
            .draggableScreenshot(
                dragToDismissState = dragToDismissState,
                onHeightOffsetChange = onHeightOffsetChange,
                onDismissRequest = onDismissRequest
            )
    )
}