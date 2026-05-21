package ru.d3rvich.jetgames.navigation.screenshots

import com.arkivanov.decompose.ComponentContext
import kotlinx.serialization.builtins.serializer

class DefaultScreenshotsComponent(
    componentContext: ComponentContext,
    initialScreenshot: Int,
    override val screenshots: List<String>,
    private val onClose: () -> Unit
) : ScreenshotsComponent, ComponentContext by componentContext {

    private var _selectedScreenshot =
        stateKeeper.consume(SELECTED_ITEM_KEY, Int.serializer()) ?: initialScreenshot
    override val selectedScreenshot: Int get() = _selectedScreenshot

    init {
        stateKeeper.register(SELECTED_ITEM_KEY, Int.serializer()) { _selectedScreenshot }
    }

    override fun onBackClick() {
        onClose()
    }

    override fun onPageChange(index: Int) {
        _selectedScreenshot = index
    }
}

private const val SELECTED_ITEM_KEY = "selected_screenshot"