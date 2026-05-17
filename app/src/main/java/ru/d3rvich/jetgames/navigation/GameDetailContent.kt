package ru.d3rvich.jetgames.navigation

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
import ru.d3rvich.detail.GameDetailScreen
import ru.d3rvich.jetgames.navigation.detail.GameDetailComponent
import ru.d3rvich.screenshots.ScreenshotsScreen

@Composable
fun GameDetailContent(component: GameDetailComponent, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        GameDetailScreen(
            gameId = component.gameId,
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
                            screenshots = screenshots,
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