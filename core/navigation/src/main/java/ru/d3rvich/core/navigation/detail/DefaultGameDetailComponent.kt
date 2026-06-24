package ru.d3rvich.core.navigation.detail

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.jetpackcomponentcontext.JetpackComponentContext
import com.arkivanov.decompose.jetpackcomponentcontext.viewModel
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.detail.GameDetailViewModel
import ru.d3rvich.core.navigation.screenshots.DefaultScreenshotsComponent
import ru.d3rvich.core.navigation.screenshots.ScreenshotsComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultGameDetailComponent(
    componentContext: JetpackComponentContext,
    override val gameId: Int,
    private val onClose: () -> Unit
) : GameDetailComponent, KoinComponent, JetpackComponentContext by componentContext {

    private val screenshotsNavigation = SlotNavigation<ScreenshotsConfig>()

    override val gameDetailViewModel: GameDetailViewModel =
        viewModel { get { parametersOf(gameId) } }

    override val screenshotsOverlay: Value<ChildSlot<*, ScreenshotsComponent>> =
        childSlot(
            source = screenshotsNavigation,
            serializer = ScreenshotsConfig.serializer(),
            handleBackButton = true,
            key = "ScreenshotsOverlay",
            childFactory = ::child
        )

    override fun onScreenshotsClick(
        selectedItem: Int,
        screenshots: List<String>
    ) {
        screenshotsNavigation.activate(ScreenshotsConfig(selectedItem, screenshots))
    }

    private fun child(
        config: ScreenshotsConfig,
        childComponent: JetpackComponentContext
    ): ScreenshotsComponent =
        DefaultScreenshotsComponent(
            componentContext = childComponent,
            initialScreenshot = config.selectedItem,
            screenshots = config.screenshots,
            onClose = { screenshotsNavigation.dismiss() })

    override fun onBackClick() {
        onClose()
    }

    @Serializable
    private data class ScreenshotsConfig(val selectedItem: Int, val screenshots: List<String>)
}