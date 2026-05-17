package ru.d3rvich.jetgames.navigation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.d3rvich.jetgames.navigation.screenshots.DefaultScreenshotsComponent
import ru.d3rvich.jetgames.navigation.screenshots.ScreenshotsComponent

class DefaultGameDetailComponent(
    componentContext: ComponentContext,
    override val gameId: Int,
    private val onClose: () -> Unit
) : GameDetailComponent, ComponentContext by componentContext {

    private val screenshotsNavigation = SlotNavigation<ScreenshotsConfig>()

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
        childComponent: ComponentContext
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