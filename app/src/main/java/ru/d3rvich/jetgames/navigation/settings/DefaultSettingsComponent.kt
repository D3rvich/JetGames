package ru.d3rvich.jetgames.navigation.settings

import com.arkivanov.decompose.ComponentContext

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    private val onCLose: () -> Unit
) : SettingsComponent, ComponentContext by componentContext {
    override fun onBackClick() {
        onCLose()
    }
}