package ru.d3rvich.feature.settings.impl.intergation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType
import ru.d3rvich.core.ui.utils.asValue
import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.feature.settings.impl.store.SettingsStore
import ru.d3rvich.feature.settings.impl.store.SettingsStoreFactory

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    settingsStoreFactory: SettingsStoreFactory,
    private val output: (SettingsComponent.Output) -> Unit,
) : SettingsComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { settingsStoreFactory.create() }

    override val models: Value<SettingsComponent.Model> = store.asValue().map(stateToModel)

    override fun setThemeType(themeType: ThemeType) {
        store.accept(SettingsStore.Intent.ThemeTypeSelected(themeType))
    }

    override fun setColorMode(colorMode: ColorMode) {
        store.accept(SettingsStore.Intent.ColorModeSelected(colorMode))
    }

    override fun onCloseClick() {
        output(SettingsComponent.Output.Finished)
    }
}