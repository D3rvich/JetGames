package ru.d3rvich.feature.main.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Factory
internal class MainComponentFactory : MainComponent.Factory, KoinComponent {
    override fun create(
        context: ComponentContext,
        output: (MainComponent.Output) -> Unit
    ): MainComponent = get<DefaultMainComponent> { parametersOf(context, output) }
}