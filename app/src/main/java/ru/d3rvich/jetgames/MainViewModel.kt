package ru.d3rvich.jetgames

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.d3rvich.core.domain.model.UserPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        userPreferencesRepository.getUserPreferences().map {
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