package ru.d3rvich.jetgames.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.d3rvich.filter.FilterScreen
import ru.d3rvich.jetgames.navigation.root.RootComponent
import ru.d3rvich.settings.SettingsScreen

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
                    is RootComponent.Child.GameDetail -> fade()
                    is RootComponent.Child.Main -> null
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
                SettingsScreen { child.component.onBackClick() }
            }

            is RootComponent.Child.Filter -> {
                FilterScreen { child.component.onBackClick() }
            }
        }
    }
}