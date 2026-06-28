package ru.d3rvich.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.d3rvich.core.navigation.detail.GameDetailComponent
import ru.d3rvich.feature.detail.GameDetailScreen

@Composable
fun GameDetailContent(component: GameDetailComponent, modifier: Modifier = Modifier) {
    GameDetailScreen(
        modifier = modifier,
        gameId = component.gameId,
        viewModel = component.gameDetailViewModel,
        navigateToScreenshotScreen = { selectedItem, screenshots ->
            component.onScreenshotsClick(
                selectedItem,
                screenshots.map { it.imageUrl }
            )
        },
        navigateBack = component::onBackClick
    )
}