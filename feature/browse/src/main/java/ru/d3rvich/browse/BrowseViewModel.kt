package ru.d3rvich.browse

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.d3rvich.browse.model.BrowseUiState
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.core.ui.base.UiEvent
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import javax.inject.Inject

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@HiltViewModel
internal class BrowseViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
//    private val getPlatformsUseCase: Provider<GetPlatformsUseCase>,
) : BaseViewModel<BrowseUiState, UiEvent, UiAction>() {
    override fun createInitialState(): BrowseUiState = BrowseUiState()

    override fun obtainEvent(event: UiEvent) {}

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getGenresUseCase.invoke().collect { status ->
                setState(currentState.copy(genres = status))
            }
        }
    }
}