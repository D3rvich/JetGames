package ru.d3rvich.core.navigation.screenshots

interface ScreenshotsComponent {
    val selectedScreenshot: Int

    val screenshots: List<String>

    fun onBackClick()

    fun onPageChange(index: Int)
}