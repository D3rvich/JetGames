package ru.d3rvich.feature.detail.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.detail.api.GameDetailComponent
import ru.d3rvich.feature.detail.impl.browser.BrowserManager
import ru.d3rvich.feature.detail.impl.store.GameDetailStoreFactory

@Factory
internal class GameDetailComponentFactory(
    private val browserManager: BrowserManager,
    private val storeFactory: GameDetailStoreFactory
) :
    GameDetailComponent.Factory {
    override fun create(
        context: ComponentContext,
        gameId: Int,
        output: (GameDetailComponent.Output) -> Unit
    ): GameDetailComponent = DefaultGameDetailComponent(
        context = context,
        gameId = gameId,
        browserManager = browserManager,
        storeFactory = storeFactory,
        output = output
    )
}