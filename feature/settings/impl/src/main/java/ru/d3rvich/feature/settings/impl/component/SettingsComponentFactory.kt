package ru.d3rvich.feature.settings.impl.component

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.settings.api.SettingsComponent

@Factory
internal class SettingsComponentFactory : SettingsComponent.Factory, KoinComponent {
    override fun create(
        componentContext: ComponentContext,
        output: (SettingsComponent.Output) -> Unit
    ): SettingsComponent = get<DefaultSettingsComponent> { parametersOf(componentContext, output) }
}