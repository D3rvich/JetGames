package ru.d3rvich.core.navigation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.asJetpackComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.d3rvich.core.navigation.main.home.DefaultHomeComponent
import ru.d3rvich.core.navigation.main.home.HomeComponent
import ru.d3rvich.feature.browse.api.BrowseComponent
import ru.d3rvich.feature.favorites.api.FavoritesComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onShowGameDetail: (gameId: Int) -> Unit,
    private val onShowSettings: () -> Unit,
    private val onShowFilter: () -> Unit,
) : MainComponent, ComponentContext by componentContext, KoinComponent {
    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MainComponent.Child>> =
        childStack(
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

    private fun child(
        config: Config,
        childComponentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        Config.Browse -> MainComponent.Child.Browse(browseComponent(childComponentContext))
        Config.Favorites -> MainComponent.Child.Favorites(favoritesComponent(childComponentContext))
        Config.Home -> MainComponent.Child.Home(homeComponent(childComponentContext))
    }

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext.asJetpackComponentContext(),
            onShowGameDetail = onShowGameDetail,
            onShowSettings = onShowSettings
        )

    private fun browseComponent(componentContext: ComponentContext): BrowseComponent {
        val factory: BrowseComponent.Factory = get()
        return factory.create(componentContext)
    }


    private fun favoritesComponent(componentContext: ComponentContext): FavoritesComponent {
        val output: (FavoritesComponent.Output) -> Unit = { output ->
            when (output) {
                is FavoritesComponent.Output.OpenGameDetail -> onShowGameDetail(output.gameId)
                FavoritesComponent.Output.OpenSettings -> onShowSettings()
            }
        }
        val factory: FavoritesComponent.Factory = get()
        return factory.create(componentContext, output)
    }

    override fun onGameClick(gameId: Int) {
        onShowGameDetail(gameId)
    }

    override fun onSettingsClick() {
        onShowSettings()
    }

    override fun onFilterClick() {
        onShowFilter()
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