package ru.d3rvich.filter.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Created by Ilya Deryabin at 03.05.2024
 */
@Composable
internal fun ChangeVisibilityContainer(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = visible,
        label = "containerVisibility",
        transitionSpec = {
            fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
        }) { isVisible ->
        if (isVisible) {
            Card(
                modifier = Modifier
                    .padding(start = 4.dp, bottom = 8.dp, end = 4.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
            ) {
                content()
            }
        } else {
            HorizontalDivider()
        }
    }
}

internal object ChangeVisibilityContainerDefaults {
    @Composable
    fun DefaultIcon(
        modifier: Modifier = Modifier,
        isOpen: Boolean,
    ) {
        val rotate by animateFloatAsState(
            targetValue = if (isOpen) -180f else 0f,
            label = "iconRotation"
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = modifier.graphicsLayer(rotationZ = rotate)
        )
    }

    @Composable
    fun ThreeStateIcon(modifier: Modifier = Modifier, iconDirection: IconDirection) {
        val rotate by animateFloatAsState(
            targetValue = when (iconDirection) {
                IconDirection.Right -> -90f
                IconDirection.Down -> -180f
                IconDirection.Up -> 0f
            },
            label = "iconRotation"
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = modifier.graphicsLayer(rotationZ = rotate)
        )
    }

}

internal enum class IconDirection {
    Right,
    Down,
    Up
}
