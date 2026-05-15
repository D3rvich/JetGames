package ru.d3rvich.jetgames.navigation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.d3rvich.jetgames.navigation.detail.GameDetailComponent
import ru.d3rvich.jetgames.navigation.main.MainComponent
import ru.d3rvich.jetgames.navigation.settings.SettingsComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed interface Child {
        class Main(val component: MainComponent) : Child
        class GameDetail(val component: GameDetailComponent) : Child
        class Settings(val component: SettingsComponent) : Child
    }
}