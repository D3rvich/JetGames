package ru.d3rvich.feature.home.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.home.api.HomeComponent

@Factory
internal class HomeComponentFactory : HomeComponent.Factory, KoinComponent {
    override fun create(
        componentContext: ComponentContext,
        output: (HomeComponent.Output) -> Unit
    ): HomeComponent = get<DefaultHomeComponent> { parametersOf(componentContext, output) }
}