package ru.d3rvich.feature.root.component

import com.arkivanov.decompose.ComponentContext
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
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.feature.detail.api.GameDetailComponent
import ru.d3rvich.feature.filter.api.FilterComponent
import ru.d3rvich.feature.main.component.MainComponent
import ru.d3rvich.feature.screenshots.api.ScreenshotsComponent
import ru.d3rvich.feature.settings.api.SettingsComponent

@Factory(binds = [DefaultRootComponent::class])
internal class DefaultRootComponent(
    @InjectedParam componentContext: ComponentContext,
    private val mainComponentFactory: MainComponent.Factory,
    private val gameDetailComponentFactory: GameDetailComponent.Factory,
    private val settingsComponentFactory: SettingsComponent.Factory,
    private val filterComponentFactory: FilterComponent.Factory,
    private val screenshotsComponentFactory: ScreenshotsComponent.Factory
) : RootComponent, ComponentContext by componentContext, BackHandlerOwner {
    private val navigation = StackNavigation<Config>()

    private val screenshotsNavigation = SlotNavigation<ScreenshotsConfig>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Main,
        handleBackButton = true,
        key = "DefaultRootComponent",
        childFactory = ::child
    )
    override val screenshotOverlay: Value<ChildSlot<*, RootComponent.ScreenshotsChild>> = childSlot(
        source = screenshotsNavigation,
        serializer = ScreenshotsConfig.serializer(),
        handleBackButton = true,
        key = "ScreenshotsSlot",
        childFactory = ::child
    )

    override fun onBackClicked() {
        navigation.pop()
    }

    private fun child(
        config: Config,
        childComponentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.GameDetail -> RootComponent.Child.GameDetail(
            gameDetailComponent(childComponentContext, config.gameId)
        )

        Config.Main -> RootComponent.Child.Main(mainComponent(childComponentContext))
        Config.Settings -> RootComponent.Child.Settings(settingsComponent(childComponentContext))
        Config.Filter -> RootComponent.Child.Filter(filterComponent(childComponentContext))
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

    private fun mainComponent(componentContext: ComponentContext): MainComponent {
        val output: (MainComponent.Output) -> Unit = { output ->
            when (output) {
                MainComponent.Output.OpenFilter -> navigation.pushNew(Config.Filter)
                is MainComponent.Output.OpenGameDetail -> navigation.pushNew(
                    Config.GameDetail(output.gameId)
                )

                MainComponent.Output.OpenSettings -> navigation.pushNew(Config.Settings)
            }
        }
        return mainComponentFactory.create(componentContext, output)
    }

    private fun gameDetailComponent(
        componentContext: ComponentContext,
        gameId: Int
    ): GameDetailComponent {
        val output: (GameDetailComponent.Output) -> Unit = { output ->
            when (output) {
                GameDetailComponent.Output.Finished -> navigation.pop()

                is GameDetailComponent.Output.OpenScreenshots -> screenshotsNavigation.activate(
                    ScreenshotsConfig(
                        selectedItem = output.selectedItem,
                        screenshots = output.items.map { it.imageUrl }
                    )
                )
            }
        }
        return gameDetailComponentFactory.create(componentContext, gameId, output)
    }

    private fun settingsComponent(componentContext: ComponentContext): SettingsComponent {
        val output: (SettingsComponent.Output) -> Unit = { output ->
            when (output) {
                SettingsComponent.Output.Finished -> navigation.pop()
            }
        }
        return settingsComponentFactory.create(componentContext = componentContext, output = output)
    }

    private fun filterComponent(componentContext: ComponentContext): FilterComponent {
        val output: (FilterComponent.Output) -> Unit = { output ->
            when (output) {
                FilterComponent.Output.Finished -> navigation.pop()
            }
        }
        return filterComponentFactory.create(context = componentContext, output = output)
    }

    private fun screenshotsComponent(
        componentContext: ComponentContext,
        items: List<String>,
        selectedItem: Int
    ): ScreenshotsComponent {
        val output: (ScreenshotsComponent.Output) -> Unit = {
            when (it) {
                ScreenshotsComponent.Output.Finished -> screenshotsNavigation.dismiss()
            }
        }
        return screenshotsComponentFactory.create(componentContext, items, selectedItem, output)
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
