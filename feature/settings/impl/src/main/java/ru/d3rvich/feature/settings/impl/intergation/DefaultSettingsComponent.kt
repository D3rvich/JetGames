package ru.d3rvich.feature.settings.impl.intergation

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType
import ru.d3rvich.feature.settings.api.SettingsComponent
import ru.d3rvich.feature.settings.impl.store.SettingsStore
import ru.d3rvich.feature.settings.impl.store.SettingsStoreFactory

@Factory(binds = [SettingsComponent::class])
internal class DefaultSettingsComponent(
    @InjectedParam componentContext: ComponentContext,
    settingsStoreFactory: SettingsStoreFactory,
    @InjectedParam private val output: (SettingsComponent.Output) -> Unit,
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

fun <T : Any> Store<*, T, *>.asValue(): Value<T> = object : Value<T>() {
    override val value: T get() = state

    override fun subscribe(observer: (T) -> Unit): Cancellation {
        val disposable = states(observer(onNext = observer))

        return Cancellation {
            disposable.dispose()
        }
    }
}