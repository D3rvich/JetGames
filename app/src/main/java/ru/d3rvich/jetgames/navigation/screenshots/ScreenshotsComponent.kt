package ru.d3rvich.jetgames.navigation.screenshots

interface ScreenshotsComponent {
    val selectedScreenshot: Int

    val screenshots: List<String>

    fun onBackClick()
}