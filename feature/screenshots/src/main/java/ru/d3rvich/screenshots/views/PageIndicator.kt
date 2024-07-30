package ru.d3rvich.screenshots.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.d3rvich.core.ui.theme.JetGamesTheme

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@Composable
internal fun PageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPageIndex: Int,
) {
    Row(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val isPageVisible = currentPageIndex == iteration
            val color by animateColorAsState(
                targetValue = if (isPageVisible) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                },
                label = "colorAnimation"
            )
            val size by animateDpAsState(
                targetValue = if (isPageVisible) {
                    20.dp
                } else {
                    12.dp
                },
                label = "sizeAnimation"
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(size)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PageIndicatorPreview() {
    JetGamesTheme {
        PageIndicator(pageCount = 5, currentPageIndex = 2)
    }
}