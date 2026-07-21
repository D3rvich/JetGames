package ru.d3rvich.feature.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.feature.browse.api.BrowseComponent
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.feature.home.api.HomeComponent

@Factory
internal class DefaultMainComponent(
    @InjectedParam componentContext: ComponentContext,
    @InjectedParam private val output: (MainComponent.Output) -> Unit,
    private val homeFactory: HomeComponent.Factory,
    private val browseFactory: BrowseComponent.Factory,
    private val favoritesFactory: FavoritesComponent.Factory
) : MainComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MainComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Favorites,
        handleBackButton = true,
        key = "DefaultMainComponent",
        childFactory = ::child
    )

    override fun onHomeClick() {
        navigation.bringToFront(Config.Home)
    }

    override fun onBrowseClick() {
        navigation.bringToFront(Config.Browse)
    }

    override fun onFavoritesCLick() {
        navigation.bringToFront(Config.Favorites)
    }

    override fun onGameClick(gameId: Int) {
        output(MainComponent.Output.OpenGameDetail(gameId))
    }

    override fun onSettingsClick() {
        output(MainComponent.Output.OpenSettings)
    }

    override fun onFilterClick() {
        output(MainComponent.Output.OpenFilter)
    }

    private fun child(
        config: Config,
        childComponentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        Config.Browse -> MainComponent.Child.Browse(browseComponent(childComponentContext))
        Config.Favorites -> MainComponent.Child.Favorites(favoritesComponent(childComponentContext))
        Config.Home -> MainComponent.Child.Home(homeComponent(childComponentContext))
    }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent {
        val output: (HomeComponent.Output) -> Unit = { output ->
            when (output) {
                HomeComponent.Output.OpenFilter -> output(MainComponent.Output.OpenFilter)
                is HomeComponent.Output.OpenGameDetail -> output(
                    MainComponent.Output.OpenGameDetail(
                        output.gameId
                    )
                )

                HomeComponent.Output.OpenSettings -> output(MainComponent.Output.OpenSettings)
            }
        }
        return homeFactory.create(componentContext, output)
    }

    private fun browseComponent(componentContext: ComponentContext): BrowseComponent =
        browseFactory.create(componentContext)


    private fun favoritesComponent(componentContext: ComponentContext): FavoritesComponent {
        val output: (FavoritesComponent.Output) -> Unit = { output ->
            when (output) {
                is FavoritesComponent.Output.OpenGameDetail -> output(
                    MainComponent.Output.OpenGameDetail(output.gameId)
                )

                FavoritesComponent.Output.OpenSettings -> output(MainComponent.Output.OpenSettings)
            }
        }
        return favoritesFactory.create(componentContext, output)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Browse : Config

        @Serializable
        data object Favorites : Config
    }
}