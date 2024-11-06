package ru.d3rvich.detail.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.d3rvich.detail.model.ScreenshotsUiState

/**
 * Created by Ilya Deryabin at 16.03.2024
 */
@Composable
internal fun ScreenshotsView(
    modifier: Modifier = Modifier,
    screenshotsState: ScreenshotsUiState,
    onItemClicked: (Int) -> Unit,
) {
    LazyRow(
        modifier = modifier.height(200.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (screenshotsState) {
            ScreenshotsUiState.Loading -> {
                items(10) {
                    ScreenshotItem(
                        isLoading = true,
                        imageUrl = "",
                    )
                }
            }

            ScreenshotsUiState.NoScreenshots -> {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No screenshots")
                    }
                }
            }

            is ScreenshotsUiState.Error -> {
                item {
                    ShowError(message = "error", modifier = Modifier.fillParentMaxWidth())
                }
            }

            is ScreenshotsUiState.Success -> {
                items(screenshotsState.screenshots) { item ->
                    val index = screenshotsState.screenshots.indexOf(item)
                    ScreenshotItem(
                        isLoading = false,
                        imageUrl = item.imageUrl,
                        onItemClicked = { onItemClicked(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenshotItem(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    imageUrl: String,
    onItemClicked: (() -> Unit)? = null,
) {
    Card(modifier = modifier.size(200.dp)) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxSize()
            )
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(role = Role.Image) { onItemClicked?.invoke() },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ShowError(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}