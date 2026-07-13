package ru.d3rvich.feature.favorites.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.favorites.api.FavoritesComponent
import ru.d3rvich.feature.favorites.impl.store.FavoritesStoreFactory

@Factory
internal class FavoritesComponentFactory(private val storeFactory: FavoritesStoreFactory) :
    FavoritesComponent.Factory {
    override fun create(
        componentContext: ComponentContext,
        output: (FavoritesComponent.Output) -> Unit
    ): FavoritesComponent = DefaultFavoritesComponent(
        componentContext = componentContext,
        storeFactory = storeFactory,
        output = output
    )
}