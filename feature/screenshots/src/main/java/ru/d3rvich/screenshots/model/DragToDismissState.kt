package ru.d3rvich.screenshots.model

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
internal fun rememberDragToDismissState(
    initialHeightOffset: Float = 0f,
    heightToDismiss: Float = DragToDismissStateDefaults.HeightToDismissPx,
): DragToDismissState = rememberSaveable(saver = DragToDismissState.Saver) {
    DragToDismissState(initialHeightOffset, heightToDismiss)
}

@Composable
internal fun Modifier.draggableScreenshot(
    dragToDismissState: DragToDismissState,
    onHeightOffsetChange: () -> Unit,
    onDismissRequest: () -> Unit,
): Modifier =
    this.then(
        Modifier.draggable(
            state = rememberDraggableState { delta ->
                dragToDismissState.heightOffset += delta
                onHeightOffsetChange.invoke()
            },
            orientation = Orientation.Vertical,
            onDragStopped = {
                if (dragToDismissState.fraction == 1f) {
                    onDismissRequest()
                } else {
                    AnimationState(initialValue = dragToDismissState.heightOffset).animateTo(
                        targetValue = 0f,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                    ) {
                        dragToDismissState.heightOffset = value
                    }
                }
            }
        )
    )

@Stable
internal class DragToDismissState internal constructor(
    initialHeightOffset: Float,
    val heightToDismiss: Float,
) {
    companion object {
        internal val Saver: Saver<DragToDismissState, *> = listSaver(
            save = { listOf(it.heightOffset, it.heightToDismiss) },
            restore = {
                DragToDismissState(initialHeightOffset = it[0], heightToDismiss = it[1])
            }
        )
    }

    var heightOffset by mutableFloatStateOf(initialHeightOffset)

    val fraction: Float
        get() = if (heightToDismiss != 0f) {
            (abs(heightOffset) / heightToDismiss).coerceIn(0f, 1f)
        } else 0f
}

internal object DragToDismissStateDefaults {
    val HeightToDismissPx
        @Composable get() = with(LocalDensity.current) { 140.dp.toPx() }
}