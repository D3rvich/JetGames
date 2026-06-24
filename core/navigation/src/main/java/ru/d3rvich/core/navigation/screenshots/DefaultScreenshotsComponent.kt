package ru.d3rvich.core.navigation.screenshots

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import kotlinx.serialization.builtins.serializer

@OptIn(ExperimentalDecomposeApi::class)
class DefaultScreenshotsComponent(
    componentContext: JetpackComponentContext,
    initialScreenshot: Int,
    override val screenshots: List<String>,
    private val onClose: () -> Unit
) : ScreenshotsComponent, JetpackComponentContext by componentContext {

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