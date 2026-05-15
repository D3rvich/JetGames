package ru.d3rvich.jetgames.navigation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.d3rvich.jetgames.navigation.main.browse.BrowseComponent
import ru.d3rvich.jetgames.navigation.main.browse.DefaultBrowseComponent
import ru.d3rvich.jetgames.navigation.main.favorites.DefaultFavoritesComponent
import ru.d3rvich.jetgames.navigation.main.favorites.FavoritesComponent
import ru.d3rvich.jetgames.navigation.main.home.DefaultHomeComponent
import ru.d3rvich.jetgames.navigation.main.home.HomeComponent

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onShowGameDetail: (gameId: Int) -> Unit,
    private val onShowSettings: () -> Unit,
    private val onShowFilter: () -> Unit,
) : MainComponent, ComponentContext by componentContext {
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
            componentContext = componentContext,
            onShowGameDetail = onShowGameDetail,
            onShowSettings = onShowSettings
        )

    private fun browseComponent(componentContext: ComponentContext): BrowseComponent =
        DefaultBrowseComponent(componentContext = componentContext)

    private fun favoritesComponent(componentContext: ComponentContext): FavoritesComponent =
        DefaultFavoritesComponent(
            componentContext = componentContext,
            onShowGameDetail = onShowGameDetail,
            onShowSettings = onShowSettings
        )

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