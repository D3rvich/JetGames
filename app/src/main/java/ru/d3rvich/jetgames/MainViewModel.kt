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
import ru.d3rvich.core.ui.model.UserPreferencesUiState
import ru.d3rvich.core.ui.model.asUiState
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {
    val userPreferences: StateFlow<UserPreferencesUiState> =
        userPreferencesRepository.getUserPreferences().map { it.asUiState() }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserPreferences().asUiState()
        )

}