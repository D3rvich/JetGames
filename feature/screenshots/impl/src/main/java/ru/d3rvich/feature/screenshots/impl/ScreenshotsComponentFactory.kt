package ru.d3rvich.feature.screenshots.impl

import com.arkivanov.decompose.ComponentContext
import org.koin.core.annotation.Factory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.d3rvich.feature.screenshots.api.ScreenshotsComponent

@Factory
internal class ScreenshotsComponentFactory : ScreenshotsComponent.Factory, KoinComponent {
    override fun create(
        componentContext: ComponentContext,
        screenshots: List<String>,
        initialPage: Int,
        output: (ScreenshotsComponent.Output) -> Unit
    ): ScreenshotsComponent = get<DefaultScreenshotsComponent> {
        parametersOf(
            componentContext,
            screenshots,
            initialPage,
            output
        )
    }
}