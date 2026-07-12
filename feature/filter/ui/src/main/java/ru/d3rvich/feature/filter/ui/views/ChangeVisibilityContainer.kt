package ru.d3rvich.feature.filter.ui.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.feature.filter.ui.R

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
            (fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()).using(
                SizeTransform(clip = false)
            )
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
        isOpen: Boolean,
        modifier: Modifier = Modifier,
    ) {
        val iconDirection = remember(isOpen) {
            if (isOpen) {
                IconDirection.Up
            } else {
                IconDirection.Down
            }
        }
        MultiStateIcon(iconDirection = iconDirection, modifier = modifier)
    }

    @Composable
    fun MultiStateIcon(iconDirection: IconDirection, modifier: Modifier = Modifier) {
        val rotate by animateFloatAsState(
            targetValue = when (iconDirection) {
                IconDirection.Right -> -90f
                IconDirection.Down -> 0f
                IconDirection.Up -> -180f
            },
            label = "iconRotation"
        )
        Icon(
            painter = painterResource(R.drawable.keyboard_arrow_down_24px),
            contentDescription = null,
            modifier = modifier.graphicsLayer { rotationZ = rotate }
        )
    }
}

internal enum class IconDirection {
    Right,
    Down,
    Up
}

@Preview(showBackground = true)
@Composable
private fun IconDirectionPreview() {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(true, false).forEach { isOpen ->
                Column {
                    Text(text = isOpen.toString())
                    ChangeVisibilityContainerDefaults.DefaultIcon(isOpen)
                }
            }
        }
        HorizontalDivider()
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconDirection.entries.forEach { direction ->
                Column {
                    Text(direction.name)
                    ChangeVisibilityContainerDefaults.MultiStateIcon(iconDirection = direction)
                }
            }
        }
    }
}
