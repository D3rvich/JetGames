package ru.d3rvich.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.d3rvich.common.R

/**
 * Created by Ilya Deryabin at 27.02.2024
 */
@Composable
fun ScrollToTopButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onButtonClick: () -> Unit,
) {
    AnimatedVisibility(visible = isVisible, modifier = modifier) {
        FloatingActionButton(onClick = onButtonClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = stringResource(R.string.scroll_to_top)
            )
        }
    }
}