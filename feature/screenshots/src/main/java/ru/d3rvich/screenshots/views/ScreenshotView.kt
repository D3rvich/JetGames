package ru.d3rvich.screenshots.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import ru.d3rvich.screenshots.model.DragToDismissState

/**
 * Created by Ilya Deryabin at 27.05.2024
 */
@Composable
internal fun ScreenshotView(
    modifier: Modifier = Modifier,
    screenshot: String,
    pageOffset: () -> Float,
    dragToDismissState: DragToDismissState,
) {
    AsyncImage(
        model = screenshot,
        contentDescription = null,
        alignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val fraction = 1f - pageOffset().coerceIn(0f, 1f)
                val lerpValue = lerp(0.5f, 1f, fraction)
                scaleX = lerpValue
                scaleY = lerpValue
                alpha = lerpValue
                translationY = dragToDismissState.heightOffset
            }
    )
}