package ru.d3rvich.core.navigation.detail

import ru.d3rvich.feature.detail.GameDetailViewModel

interface GameDetailComponent {
    val gameId: Int

    val gameDetailViewModel: GameDetailViewModel

    fun onScreenshotsClick(selectedItem: Int, screenshots: List<String>)

    fun onBackClick()

    sealed interface Output {
        data object Finished : Output
        data class OpenScreenshotsAt(val selectedItem: Int, val items: List<String>) : Output
    }
}