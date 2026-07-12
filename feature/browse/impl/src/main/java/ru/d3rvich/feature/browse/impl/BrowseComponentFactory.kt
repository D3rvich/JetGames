package ru.d3rvich.feature.browse.impl

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.browse.api.BrowseComponent

@Factory(binds = [BrowseComponent.Factory::class])
internal class BrowseComponentFactory : BrowseComponent.Factory, KoinComponent {
    override fun create(component: ComponentContext): BrowseComponent =
        get { parametersOf(component) }
}