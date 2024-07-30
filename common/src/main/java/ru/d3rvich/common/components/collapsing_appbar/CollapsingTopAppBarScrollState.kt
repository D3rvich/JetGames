package ru.d3rvich.common.components.collapsing_appbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberCollapsingTopAppBarScrollState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f,
): CollapsingTopAppBarScrollState = rememberSaveable(saver = CollapsingTopAppBarScrollState.Saver) {
    CollapsingTopAppBarScrollState(
        initialHeightOffsetLimit,
        initialHeightOffset,
        initialContentOffset
    )
}

@Stable
class CollapsingTopAppBarScrollState internal constructor(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
    initialContentOffset: Float,
) {
    companion object {
        internal val Saver: Saver<CollapsingTopAppBarScrollState, *> = listSaver(
            save = { listOf(it.heightOffsetLimit, it.heightOffset, it.contentOffset) },
            restore = {
                CollapsingTopAppBarScrollState(
                    initialHeightOffsetLimit = it[0],
                    initialHeightOffset = it[1],
                    initialContentOffset = it[2],
                )
            }
        )
    }

    var heightOffsetLimit by mutableFloatStateOf(initialHeightOffsetLimit)

    var heightOffset: Float
        get() = _heightOffset.floatValue
        set(newOffset) {
            _heightOffset.floatValue = newOffset.coerceIn(
                minimumValue = heightOffsetLimit,
                maximumValue = 0f
            )
        }

    var contentOffset by mutableFloatStateOf(initialContentOffset)

    val collapsedFraction: Float
        get() = if (heightOffsetLimit != 0f) {
            heightOffset / heightOffsetLimit
        } else {
            0f
        }

    private var _heightOffset = mutableFloatStateOf(initialHeightOffset)
}