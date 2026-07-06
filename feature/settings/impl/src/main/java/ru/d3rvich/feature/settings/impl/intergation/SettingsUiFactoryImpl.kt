package ru.d3rvich.feature.settings.impl.intergation

import androidx.compose.runtime.Composable
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.feature.settings.api.SettingsUiFactory
import ru.d3rvich.feature.settings.impl.SettingsContent

@Factory
internal class SettingsUiFactoryImpl : SettingsUiFactory {
    @Composable
    override fun Content(component: SettingsComponent) {
        SettingsContent(component)
    }
}