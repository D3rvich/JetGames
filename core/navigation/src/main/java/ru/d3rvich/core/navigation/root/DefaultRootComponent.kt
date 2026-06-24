package ru.d3rvich.core.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.asJetpackComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable
import ru.d3rvich.core.navigation.detail.DefaultGameDetailComponent
import ru.d3rvich.core.navigation.detail.GameDetailComponent
import ru.d3rvich.core.navigation.filter.DefaultFilterComponent
import ru.d3rvich.core.navigation.filter.FilterComponent
import ru.d3rvich.core.navigation.main.DefaultMainComponent
import ru.d3rvich.core.navigation.main.MainComponent
import ru.d3rvich.core.navigation.root.RootComponent.Child.*
import ru.d3rvich.core.navigation.settings.DefaultSettingsComponent
import ru.d3rvich.core.navigation.settings.SettingsComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultRootComponent(componentContext: ComponentContext) : RootComponent,
    ComponentContext by componentContext, BackHandlerOwner {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Main,
            handleBackButton = true,
            key = "DefaultRootComponent",
            childFactory = ::child
        )

    private fun child(
        config: Config,
        childComponentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.GameDetail -> GameDetail(
            gameDetailComponent(childComponentContext, config.gameId)
        )

        Config.Main -> Main(mainComponent(childComponentContext))
        Config.Settings -> Settings(settingsComponent(childComponentContext))
        Config.Filter -> Filter(filterComponent(childComponentContext))
    }

    private fun mainComponent(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(
            componentContext = componentContext,
            onShowGameDetail = { navigation.pushNew(Config.GameDetail(it)) },
            onShowSettings = { navigation.pushNew(Config.Settings) },
            onShowFilter = { navigation.pushNew(Config.Filter) })

    private fun gameDetailComponent(
        componentContext: ComponentContext,
        gameId: Int
    ): GameDetailComponent =
        DefaultGameDetailComponent(
            componentContext = componentContext.asJetpackComponentContext(),
            gameId = gameId,
            onClose = { navigation.pop() })

    private fun settingsComponent(componentContext: ComponentContext): SettingsComponent =
        DefaultSettingsComponent(
            componentContext = componentContext.asJetpackComponentContext(),
            onCLose = { navigation.pop() })

    private fun filterComponent(componentContext: ComponentContext): FilterComponent =
        DefaultFilterComponent(
            componentContext = componentContext.asJetpackComponentContext(),
            onClose = { navigation.pop() })

    override fun onBackClicked() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data class GameDetail(val gameId: Int) : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Filter : Config
    }
}