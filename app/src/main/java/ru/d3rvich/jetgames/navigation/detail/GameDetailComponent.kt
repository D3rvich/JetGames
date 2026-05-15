package ru.d3rvich.jetgames.navigation.detail

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import ru.d3rvich.jetgames.navigation.screenshots.ScreenshotsComponent

interface GameDetailComponent {
    val gameId: Int

    val screenshotsOverlay: Value<ChildSlot<*, ScreenshotsComponent>>

    fun onScreenshotsClick(selectedItem: Int, screenshots: List<String>)

    fun onBackClick()
}