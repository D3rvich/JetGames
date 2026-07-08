package ru.d3rvich.feature.screenshots.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

@Stable
interface ScreenshotsComponent {
    val screenshots: List<String>
    val currentPage: Int
    val showWidgets: Value<Boolean>

    fun setShowWidgets(isShow: Boolean)

    fun safePage(page: Int)

    fun onClose()

    sealed interface Output {
        data object Finished : Output
    }

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            screenshots: List<String>,
            initialPage: Int,
            output: (Output) -> Unit
        ): ScreenshotsComponent
    }
}