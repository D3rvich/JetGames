package ru.d3rvich.core.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.d3rvich.core.navigation.root.RootComponent
import ru.d3rvich.feature.filter.impl.FilterScreen
import ru.d3rvich.feature.screenshots.ui.ScreenshotsContent
import ru.d3rvich.feature.settings.ui.SettingsContent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(
    rootComponent: RootComponent,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    Children(
        stack = rootComponent.stack,
        modifier = modifier,
        animation = predictiveBackAnimation(
            backHandler = rootComponent.backHandler,
            fallbackAnimation = stackAnimation { child ->
                when (child.instance) {
                    is RootComponent.Child.Filter, is RootComponent.Child.Settings -> slide()
                    else -> fade()
                }
            },
            selector = { backEvent, _, _ -> androidPredictiveBackAnimatableV2(backEvent) },
            onBack = { rootComponent.onBackClicked() })
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.GameDetail -> {
                GameDetailContent(child.component)
            }

            is RootComponent.Child.Main -> {
                MainContent(child.component, windowSizeClass)
            }

            is RootComponent.Child.Settings -> {
                SettingsContent(child.component)
            }

            is RootComponent.Child.Filter -> {
                FilterScreen(
                    onNavigateBack = child.component::onBackClick,
                    viewModel = child.component.filterViewModel
                )
            }
        }
    }
    val screenshotSlot by rootComponent.screenshotOverlay.subscribeAsState()
    AnimatedContent(
        screenshotSlot,
        transitionSpec = { fadeIn() togetherWith fadeOut() }) { slot ->
        Box(Modifier.fillMaxSize()) {
            slot.child?.let { child ->
                ScreenshotsContent(child.instance.component)
            }
        }
    }
}