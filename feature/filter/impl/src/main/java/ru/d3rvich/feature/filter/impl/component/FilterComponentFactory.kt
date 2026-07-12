package ru.d3rvich.feature.filter.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.filter.api.FilterComponent
import ru.d3rvich.feature.filter.impl.store.FilterStoreFactory

@Factory
internal class FilterComponentFactory(private val storeFactory: FilterStoreFactory) :
    FilterComponent.Factory {
    override fun create(
        context: ComponentContext,
        output: (FilterComponent.Output) -> Unit
    ): FilterComponent = DefaultFilterComponent(
        storeFactory = storeFactory,
        context = context,
        output = output
    )
}