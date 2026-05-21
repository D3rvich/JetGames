package ru.d3rvich.jetgames.navigation.main.favorites

import com.arkivanov.decompose.ComponentContext

class DefaultFavoritesComponent(
    componentContext: ComponentContext,
    private val onShowGameDetail: (gameId: Int) -> Unit,
    private val onShowSettings: () -> Unit
) : FavoritesComponent, ComponentContext by componentContext {
    override fun onGameClick(gameId: Int) {
        onShowGameDetail(gameId)
    }

    override fun inSettingsClick() {
        onShowSettings()
    }
}