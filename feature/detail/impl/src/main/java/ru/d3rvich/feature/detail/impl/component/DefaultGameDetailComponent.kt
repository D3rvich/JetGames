package ru.d3rvich.feature.detail.impl.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.ui.utils.asValue
import ru.d3rvich.feature.detail.api.GameDetailComponent
import ru.d3rvich.feature.detail.impl.browser.BrowserManager
import ru.d3rvich.feature.detail.impl.store.GameDetailStore
import ru.d3rvich.feature.detail.impl.store.GameDetailStoreFactory

@Factory(binds = [DefaultGameDetailComponent::class])
internal class DefaultGameDetailComponent(
    @InjectedParam context: ComponentContext,
    @InjectedParam override val gameId: Int,
    @InjectedParam private val output: (GameDetailComponent.Output) -> Unit,
    private val storeFactory: GameDetailStoreFactory,
    private val browserManager: BrowserManager
) : GameDetailComponent, ComponentContext by context {
    private val store = instanceKeeper.getStore { storeFactory.create(gameId) }

    override val model: Value<GameDetailComponent.Model> =
        store.asValue().map(stateToModel)

    override fun refresh() {
        store.accept(GameDetailStore.Intent.OnRefresh)
    }

    override fun setFavorites(isFavorite: Boolean) {
        store.accept(GameDetailStore.Intent.OnFavoriteChange(isFavorite))
    }

    override fun openScreenshots(
        selectedItem: Int,
        items: List<ScreenshotEntity>
    ) {
        output(GameDetailComponent.Output.OpenScreenshots(selectedItem, items))
    }

    override fun openGameStore(url: String) {
        browserManager.launchUrl(url)
    }

    override fun close() {
        output(GameDetailComponent.Output.Finished)
    }
}

