package ru.d3rvich.jetgames.navigation.main.home

import com.arkivanov.decompose.ComponentContext

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onShowGameDetail: (gameId: Int) -> Unit,
    private val onShowSettings: () -> Unit
) : HomeComponent, ComponentContext by componentContext {
    override fun onGameClick(gameId: Int) {
        onShowGameDetail(gameId)
    }

    override fun onSettingsClick() {
        onShowSettings()
    }
}