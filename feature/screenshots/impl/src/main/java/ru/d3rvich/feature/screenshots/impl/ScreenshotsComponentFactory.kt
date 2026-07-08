package ru.d3rvich.feature.screenshots.impl

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import ru.d3rvich.feature.screenshots.api.ScreenshotsComponent

@Factory
internal class ScreenshotsComponentFactory :
    ScreenshotsComponent.Factory {
    override fun create(
        componentContext: ComponentContext,
        screenshots: List<String>,
        initialPage: Int,
        output: (ScreenshotsComponent.Output) -> Unit
    ): ScreenshotsComponent = DefaultScreenshotsComponent(
        componentContext = componentContext,
        initialPage = initialPage,
        screenshots = screenshots,
        output = output
    )
}