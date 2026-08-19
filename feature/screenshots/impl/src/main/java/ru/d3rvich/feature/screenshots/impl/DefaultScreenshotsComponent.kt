package ru.d3rvich.feature.screenshots.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.feature.screenshots.api.ScreenshotsComponent

@Factory(binds = [DefaultScreenshotsComponent::class])
internal class DefaultScreenshotsComponent(
    @InjectedParam componentContext: ComponentContext,
    @InjectedParam initialPage: Int,
    @InjectedParam override val screenshots: List<String>,
    @InjectedParam private val output: (ScreenshotsComponent.Output) -> Unit,
) : ScreenshotsComponent, ComponentContext by componentContext {

    private val _showWidgets = MutableValue(true)
    override val showWidgets: Value<Boolean> = _showWidgets

    private var _currentPage: Int = initialPage
    override val currentPage: Int = _currentPage

    private val backCallback = BackCallback {
        output(ScreenshotsComponent.Output.Finished)
    }

    init {
        backHandler.register(backCallback)
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
