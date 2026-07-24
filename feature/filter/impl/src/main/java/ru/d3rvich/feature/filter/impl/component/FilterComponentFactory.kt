package ru.d3rvich.feature.filter.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.filter.api.FilterComponent

@Factory
internal class FilterComponentFactory : FilterComponent.Factory, KoinComponent {
    override fun create(
        context: ComponentContext,
        output: (FilterComponent.Output) -> Unit
    ): FilterComponent = get<DefaultFilterComponent> { parametersOf(context, output) }
}