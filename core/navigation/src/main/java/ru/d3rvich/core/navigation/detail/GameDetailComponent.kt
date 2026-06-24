package ru.d3rvich.core.navigation.detail

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import ru.d3rvich.core.navigation.screenshots.ScreenshotsComponent
import ru.d3rvich.feature.detail.GameDetailViewModel

interface GameDetailComponent {
    val gameId: Int

    val gameDetailViewModel: GameDetailViewModel

    val screenshotsOverlay: Value<ChildSlot<*, ScreenshotsComponent>>

    fun onScreenshotsClick(selectedItem: Int, screenshots: List<String>)

    fun onBackClick()
}