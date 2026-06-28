package ru.d3rvich.core.navigation.detail

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.detail.GameDetailViewModel

@OptIn(ExperimentalDecomposeApi::class)
class DefaultGameDetailComponent(
    componentContext: JetpackComponentContext,
    override val gameId: Int,
    private val output: (GameDetailComponent.Output) -> Unit
) : GameDetailComponent, KoinComponent, JetpackComponentContext by componentContext {

    override val gameDetailViewModel: GameDetailViewModel =
        viewModel { get { parametersOf(gameId) } }

    override fun onScreenshotsClick(selectedItem: Int, screenshots: List<String>) {
        output(GameDetailComponent.Output.OpenScreenshotsAt(selectedItem, screenshots))
    }

    override fun onBackClick() {
        output(GameDetailComponent.Output.Finished)
    }
}