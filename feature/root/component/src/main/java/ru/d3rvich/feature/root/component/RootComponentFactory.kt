package ru.d3rvich.feature.root.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Factory
internal class RootComponentFactory : RootComponent.Factory, KoinComponent {
    override fun create(componentContext: ComponentContext): RootComponent =
        get<DefaultRootComponent> { parametersOf(componentContext) }
}