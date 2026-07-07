package ru.d3rvich.feature.settings.impl.intergation

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.feature.settings.impl.store.SettingsStoreFactory

@Factory
internal class SettingsComponentFactory(private val storeFactory: SettingsStoreFactory) :
    SettingsComponent.Factory {
    override fun create(
        componentContext: ComponentContext,
        output: (SettingsComponent.Output) -> Unit
    ): SettingsComponent = DefaultSettingsComponent(
        componentContext = componentContext,
        settingsStoreFactory = storeFactory,
        output = output
    )
}