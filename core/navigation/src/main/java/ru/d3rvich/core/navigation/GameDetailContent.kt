package ru.d3rvich.core.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.collections.immutable.toImmutableList
import ru.d3rvich.feature.detail.GameDetailScreen
import ru.d3rvich.feature.screenshots.ScreenshotsScreen
import ru.d3rvich.core.navigation.detail.GameDetailComponent

@Composable
fun GameDetailContent(component: GameDetailComponent, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        GameDetailScreen(
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

        val screenshotsSlot by component.screenshotsOverlay.subscribeAsState()

        AnimatedContent(
            screenshotsSlot,
            transitionSpec = { fadeIn() togetherWith fadeOut() }) { screenshotsSlot ->
            Box(Modifier.fillMaxSize()) {
                screenshotsSlot.child?.let { screenshotsComponent ->
                    with(screenshotsComponent.instance) {
                        ScreenshotsScreen(
                            screenshots = screenshots.toImmutableList(),
                            selectedItem = selectedScreenshot,
                            onPageChange = ::onPageChange,
                            onBackPressed = ::onBackClick
                        )
                    }
                }
            }
        }
    }
}