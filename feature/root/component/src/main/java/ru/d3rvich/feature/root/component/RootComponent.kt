package ru.d3rvich.feature.root.component

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import ru.d3rvich.feature.detail.api.GameDetailComponent
import ru.d3rvich.feature.filter.api.FilterComponent
import ru.d3rvich.feature.main.component.MainComponent
import ru.d3rvich.feature.screenshots.api.ScreenshotsComponent
import ru.d3rvich.feature.settings.api.SettingsComponent

@Stable
interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    val screenshotOverlay: Value<ChildSlot<*, ScreenshotsChild>>

    fun onBackClicked()

    sealed interface Child {
        class Main(val component: MainComponent) : Child
        class GameDetail(val component: GameDetailComponent) : Child
        class Settings(val component: SettingsComponent) : Child
        class Filter(val component: FilterComponent) : Child
    }

    class ScreenshotsChild(val component: ScreenshotsComponent)

    interface Factory {
        fun create(componentContext: ComponentContext): RootComponent
    }
}

