package ru.d3rvich.jetgames.navigation.main.home

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import org.koin.core.component.KoinComponent
import ru.d3rvich.feature.home.HomeViewModel

@OptIn(ExperimentalDecomposeApi::class)
class DefaultHomeComponent(
    componentContext: JetpackComponentContext,
    private val onShowGameDetail: (gameId: Int) -> Unit,
    private val onShowSettings: () -> Unit
) : HomeComponent, KoinComponent, JetpackComponentContext by componentContext {
    override val homeViewModel: HomeViewModel = viewModel { getKoin().get() }

    override fun onGameClick(gameId: Int) {
        onShowGameDetail(gameId)
    }

    override fun onSettingsClick() {
        onShowSettings()
    }
}