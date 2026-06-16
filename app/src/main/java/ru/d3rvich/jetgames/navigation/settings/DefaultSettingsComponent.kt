package ru.d3rvich.jetgames.navigation.settings

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.d3rvich.feature.settings.SettingsViewModel

@OptIn(ExperimentalDecomposeApi::class)
class DefaultSettingsComponent(
    componentContext: JetpackComponentContext,
    private val onCLose: () -> Unit
) : SettingsComponent, KoinComponent, JetpackComponentContext by componentContext {
    override val settingsViewModel: SettingsViewModel = viewModel { get() }

    override fun onBackClick() {
        onCLose()
    }
}