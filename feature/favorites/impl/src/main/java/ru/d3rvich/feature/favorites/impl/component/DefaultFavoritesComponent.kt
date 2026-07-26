package ru.d3rvich.feature.favorites.impl.component

import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.ui.model.toGameUiModel
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.feature.favorites.impl.store.FavoritesStore
import ru.d3rvich.feature.favorites.impl.store.FavoritesStoreFactory

@Factory(binds = [DefaultFavoritesComponent::class])
internal class DefaultFavoritesComponent(
    @InjectedParam componentContext: ComponentContext,
    @InjectedParam private val output: (FavoritesComponent.Output) -> Unit,
    private val storeFactory: FavoritesStoreFactory
) : FavoritesComponent, ComponentContext by componentContext {
    private val store: FavoritesStore = instanceKeeper.getStore { storeFactory.create() }

    override val model: FavoritesComponent.Model =
        FavoritesComponent.Model(games = store.state.games.map { pagingData ->
            pagingData.map(GameEntity::toGameUiModel)
        })

    override fun gameClicked(gameId: Int) {
        output(FavoritesComponent.Output.OpenGameDetail(gameId))
    }

    override fun settingsClicked() {
        output(FavoritesComponent.Output.OpenSettings)
    }
}