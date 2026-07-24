package ru.d3rvich.feature.favorites.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.favorites.api.FavoritesComponent

@Factory
internal class FavoritesComponentFactory : FavoritesComponent.Factory, KoinComponent {
    override fun create(
        componentContext: ComponentContext,
        output: (FavoritesComponent.Output) -> Unit
    ): FavoritesComponent =
        get<DefaultFavoritesComponent> { parametersOf(componentContext, output) }
}