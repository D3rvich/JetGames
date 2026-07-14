package ru.d3rvich.feature.home.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.home.api.HomeComponent
import ru.d3rvich.feature.home.impl.store.HomeStoreFactory

@Factory
internal class HomeComponentFactory(private val storeFactory: HomeStoreFactory): HomeComponent.Factory {
    override fun create(
        componentContext: ComponentContext,
        output: (HomeComponent.Output) -> Unit
    ): HomeComponent = DefaultHomeComponent(componentContext, storeFactory, output)
}