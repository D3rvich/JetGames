package ru.d3rvich.jetgames.navigation.screenshots

import com.arkivanov.decompose.ComponentContext

class DefaultScreenshotsComponent(
    componentContext: ComponentContext,
    override val selectedScreenshot: Int,
    override val screenshots: List<String>,
    private val onClose: () -> Unit
) : ScreenshotsComponent, ComponentContext by componentContext {
    override fun onBackClick() {
        onClose()
    }
}