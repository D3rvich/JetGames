package ru.d3rvich.feature.favorites.impl.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.feature.favorites.impl.store.FavoritesStore
import ru.d3rvich.feature.favorites.impl.store.FavoritesStoreFactory

@Factory(binds = [DefaultFavoritesComponent::class])
internal class DefaultFavoritesComponent(
    @InjectedParam componentContext: ComponentContext,
    @InjectedParam private val output: (FavoritesComponent.Output) -> Unit,
    private val storeFactory: FavoritesStoreFactory
) : FavoritesComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val store: FavoritesStore = instanceKeeper.getStore { storeFactory.create(scope) }

    init {
        doOnDestroy(scope::cancel)
    }

    override val model: FavoritesComponent.Model = FavoritesComponent.Model(store.state.games)

    override fun gameClicked(gameId: Int) {
        output(FavoritesComponent.Output.OpenGameDetail(gameId))
    }

    override fun settingsClicked() {
        output(FavoritesComponent.Output.OpenSettings)
    }
}