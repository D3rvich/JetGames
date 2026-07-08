package ru.d3rvich.feature.screenshots.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.serialization.builtins.serializer
import ru.d3rvich.feature.screenshots.api.ScreenshotsComponent

internal class DefaultScreenshotsComponent(
    componentContext: ComponentContext,
    initialPage: Int,
    override val screenshots: List<String>,
    private val output: (ScreenshotsComponent.Output) -> Unit,
) : ScreenshotsComponent, ComponentContext by componentContext {

    private val savedShowWidgets =
        stateKeeper.consume(SHOW_WIDGETS_KEY, Boolean.serializer()) ?: true
    private val _showWidgets = MutableValue(savedShowWidgets)
    override val showWidgets: Value<Boolean> = _showWidgets

    private var _currentPage: Int =
        stateKeeper.consume(CURRENT_PAGE_KEY, Int.serializer()) ?: initialPage

    override val currentPage: Int = _currentPage

    private val backCallback = BackCallback {
        output(ScreenshotsComponent.Output.Finished)
    }

    init {
        backHandler.register(backCallback)
        stateKeeper.register(CURRENT_PAGE_KEY, Int.serializer()) { _currentPage }
        stateKeeper.register(SHOW_WIDGETS_KEY, Boolean.serializer()) { _showWidgets.value }
    }

    override fun setShowWidgets(isShow: Boolean) {
        _showWidgets.value = isShow
    }

    override fun onClose() {
        output(ScreenshotsComponent.Output.Finished)
    }

    override fun safePage(page: Int) {
        _currentPage = page
    }
}

private const val CURRENT_PAGE_KEY = "current_page"
private const val SHOW_WIDGETS_KEY = "show_widgets"