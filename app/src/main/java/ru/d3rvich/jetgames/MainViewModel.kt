package ru.d3rvich.jetgames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import ru.d3rvich.core.domain.repositories.ColorModeType
import ru.d3rvich.core.domain.repositories.SettingsData
import ru.d3rvich.core.domain.repositories.SettingsRepository
import ru.d3rvich.core.domain.repositories.ThemeType
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(settingsRepository: SettingsRepository) :
    ViewModel() {
    val settings = settingsRepository.getSettingsData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SettingsData(ThemeType.System, ColorModeType.Default)
    )
}