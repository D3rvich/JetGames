package ru.d3rvich.feature.main.component

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import ru.d3rvich.feature.browse.api.BrowseComponent
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.feature.home.api.HomeComponent

@Stable
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

    sealed interface Output {
        data class OpenGameDetail(val gameId: Int) : Output
        data object OpenSettings : Output
        data object OpenFilter : Output
    }

    interface Factory {
        fun create(context: ComponentContext, output: (Output) -> Unit): MainComponent
    }
}