package ru.d3rvich.jetgames.navigation.main.favorites

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import org.koin.core.component.KoinComponent
import ru.d3rvich.feature.favorites.FavoritesViewModel

@OptIn(ExperimentalDecomposeApi::class)
class DefaultFavoritesComponent(
    componentContext: JetpackComponentContext,
    private val onShowGameDetail: (gameId: Int) -> Unit,
    private val onShowSettings: () -> Unit,
) : FavoritesComponent, KoinComponent, JetpackComponentContext by componentContext {
    override val favoritesViewModel: FavoritesViewModel = viewModel { getKoin().get() }

    override fun onGameClick(gameId: Int) {
        onShowGameDetail(gameId)
    }

    override fun inSettingsClick() {
        onShowSettings()
    }
}