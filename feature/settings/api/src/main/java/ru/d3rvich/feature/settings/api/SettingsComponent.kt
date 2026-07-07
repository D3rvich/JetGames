package ru.d3rvich.feature.settings.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import ru.d3rvich.core.model.ColorMode
import ru.d3rvich.core.model.ThemeType

@Stable
interface SettingsComponent {

    val models: Value<Model>

    fun setThemeType(themeType: ThemeType)

    fun setColorMode(colorMode: ColorMode)

    fun onCloseClick()

    @Immutable
    sealed interface Model {
        data object Loading : Model

        data class Settings(
            val themeType: ThemeType,
            val colorMode: ColorMode,
            val inDynamicColorSupported: Boolean
        ) : Model
    }

    sealed interface Output {
        data object Finished : Output
    }

    fun interface Factory {
        fun create(
            componentContext: ComponentContext,
            output: (Output) -> Unit
        ): SettingsComponent
    }
}

fun interface SettingsUiFactory {
    @Composable
    fun Content(component: SettingsComponent)
}