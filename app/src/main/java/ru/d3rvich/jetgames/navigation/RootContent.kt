package ru.d3rvich.jetgames.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.d3rvich.filter.FilterScreen
import ru.d3rvich.jetgames.navigation.root.RootComponent
import ru.d3rvich.settings.SettingsScreen

@Composable
fun RootContent(
    rootComponent: RootComponent,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    Children(rootComponent.stack, modifier) {
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