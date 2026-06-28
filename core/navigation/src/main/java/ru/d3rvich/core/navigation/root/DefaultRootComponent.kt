package ru.d3rvich.core.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.asJetpackComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parameterSetOf
import ru.d3rvich.core.navigation.detail.DefaultGameDetailComponent
import ru.d3rvich.core.navigation.detail.GameDetailComponent
import ru.d3rvich.core.navigation.filter.DefaultFilterComponent
import ru.d3rvich.core.navigation.filter.FilterComponent
import ru.d3rvich.core.navigation.main.DefaultMainComponent
import ru.d3rvich.core.navigation.main.MainComponent
import ru.d3rvich.core.navigation.root.RootComponent.Child.Filter
import ru.d3rvich.core.navigation.root.RootComponent.Child.GameDetail
import ru.d3rvich.core.navigation.root.RootComponent.Child.Main
import ru.d3rvich.core.navigation.root.RootComponent.Child.Settings
import ru.d3rvich.core.navigation.screenshots.DefaultScreenshotsComponent
import ru.d3rvich.core.navigation.screenshots.ScreenshotsComponent
import ru.d3rvich.feature.settings.api.SettingsComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultRootComponent(componentContext: ComponentContext) : RootComponent, KoinComponent,
    ComponentContext by componentContext, BackHandlerOwner {
    private val navigation = StackNavigation<Config>()

    private val screenshotsNavigation = SlotNavigation<ScreenshotsConfig>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Main,
            handleBackButton = true,
            key = "DefaultRootComponent",
            childFactory = ::child
        )

    override val screenshotOverlay: Value<ChildSlot<*, RootComponent.ScreenshotsChild>> =
        childSlot(
            source = screenshotsNavigation,
            serializer = ScreenshotsConfig.serializer(),
            handleBackButton = true,
            key = "ScreenshotsSlot",
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

    private fun child(
        config: ScreenshotsConfig,
        componentContext: ComponentContext
    ): RootComponent.ScreenshotsChild = RootComponent.ScreenshotsChild(
        screenshotsComponent(
            componentContext = componentContext,
            items = config.screenshots,
            selectedItem = config.selectedItem
        )
    )

    private fun mainComponent(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(
            componentContext = componentContext,
            onShowGameDetail = { navigation.pushNew(Config.GameDetail(it)) },
            onShowSettings = { navigation.pushNew(Config.Settings) },
            onShowFilter = { navigation.pushNew(Config.Filter) })

    private fun gameDetailComponent(
        componentContext: ComponentContext,
        gameId: Int
    ): GameDetailComponent {
        val output: (GameDetailComponent.Output) -> Unit = { output ->
            when (output) {
                GameDetailComponent.Output.Finished -> navigation.pop()

                is GameDetailComponent.Output.OpenScreenshotsAt -> {
                    screenshotsNavigation.activate(
                        ScreenshotsConfig(
                            selectedItem = output.selectedItem,
                            screenshots = output.items
                        )
                    )
                }
            }
        }
        return DefaultGameDetailComponent(
            componentContext = componentContext.asJetpackComponentContext(),
            gameId = gameId,
            output = output
        )
    }

    private fun settingsComponent(componentContext: ComponentContext): SettingsComponent {
        val output: (SettingsComponent.Output) -> Unit = { output ->
            when (output) {
                SettingsComponent.Output.Finished -> navigation.pop()
            }
        }
        return get { parameterSetOf(componentContext, output) }
    }

    private fun filterComponent(componentContext: ComponentContext): FilterComponent =
        DefaultFilterComponent(
            componentContext = componentContext.asJetpackComponentContext(),
            onClose = { navigation.pop() })

    private fun screenshotsComponent(
        componentContext: ComponentContext,
        items: List<String>,
        selectedItem: Int
    ): ScreenshotsComponent = DefaultScreenshotsComponent(
        componentContext = componentContext.asJetpackComponentContext(),
        initialScreenshot = selectedItem,
        screenshots = items,
        onClose = { screenshotsNavigation.dismiss() })

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

    @Serializable
    private data class ScreenshotsConfig(val selectedItem: Int, val screenshots: List<String>)
}