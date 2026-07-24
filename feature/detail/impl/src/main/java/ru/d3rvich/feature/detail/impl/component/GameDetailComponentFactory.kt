package ru.d3rvich.feature.detail.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.detail.api.GameDetailComponent

@Factory
internal class GameDetailComponentFactory : GameDetailComponent.Factory, KoinComponent {
    override fun create(
        context: ComponentContext,
        gameId: Int,
        output: (GameDetailComponent.Output) -> Unit
    ): GameDetailComponent =
        get<DefaultGameDetailComponent> { parametersOf(context, gameId, output) }
}