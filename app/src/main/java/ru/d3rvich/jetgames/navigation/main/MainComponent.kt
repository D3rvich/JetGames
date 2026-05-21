package ru.d3rvich.jetgames.navigation.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.d3rvich.jetgames.navigation.main.browse.BrowseComponent
import ru.d3rvich.jetgames.navigation.main.favorites.FavoritesComponent
import ru.d3rvich.jetgames.navigation.main.home.HomeComponent

interface MainComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onHomeClick()

    fun onBrowseClick()

    fun onFavoritesCLick()

    fun onGameClick(gameId: Int)

    fun onSettingsClick()

    fun onFilterClick()

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class Browse(val component: BrowseComponent) : Child
        class Favorites(val component: FavoritesComponent) : Child
    }
}