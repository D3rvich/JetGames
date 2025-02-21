package ru.d3rvich.settings

import android.os.Build
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.d3rvich.core.domain.repositories.ColorModeType
import ru.d3rvich.core.domain.repositories.SettingsRepository
import ru.d3rvich.core.domain.repositories.ThemeType
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.core.ui.base.UiEvent
import ru.d3rvich.core.ui.base.UiState
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository) :
    BaseViewModel<SettingsUiState, SettingsUiEvent, UiAction>() {

    override fun createInitialState(): SettingsUiState = SettingsUiState.Loading

    override fun obtainEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.UpdateColorMode -> {
                    settingsRepository.setCurrentColorMode(event.colorModeType)
                }

                is SettingsUiEvent.UpdateThemeType -> {
                    settingsRepository.setCurrentTheme(event.themeType)
                }
            }
        }
    }

    private val isDynamicColorSupported by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    init {
        viewModelScope.launch {
            launch {
                settingsRepository.getSettingsData().collect { data ->
                    setState(
                        SettingsUiState.Settings(
                            themeType = data.theme,
                            colorModeType = data.colorMode,
                            inDynamicColorSupported = isDynamicColorSupported
                        )
                    )
                }
            }
        }
    }
}

sealed interface SettingsUiState : UiState {
    data object Loading : SettingsUiState
    class Settings(
        val themeType: ThemeType,
        val colorModeType: ColorModeType,
        val inDynamicColorSupported: Boolean
    ) : SettingsUiState
}

sealed interface SettingsUiEvent : UiEvent {
    class UpdateThemeType(val themeType: ThemeType) :
        SettingsUiEvent

    class UpdateColorMode(val colorModeType: ColorModeType) : SettingsUiEvent
}