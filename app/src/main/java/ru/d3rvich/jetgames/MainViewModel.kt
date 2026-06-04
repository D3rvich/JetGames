package ru.d3rvich.jetgames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel(userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        userPreferencesRepository.getUserPreferences().map {
            delay(100.milliseconds)
            MainActivityUiState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MainActivityUiState.Loading
        )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userPreferences: UserPreferences) : MainActivityUiState

    fun shouldKeepSplash() = this is Loading
}