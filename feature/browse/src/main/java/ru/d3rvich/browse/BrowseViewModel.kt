package ru.d3rvich.browse

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.browse.model.BrowseUiState
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import ru.d3rvich.core.domain.usecases.GetPlatformsUseCase
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.core.ui.base.UiEvent

/**
 * Created by Ilya Deryabin at 05.06.2024
 */
@KoinViewModel
internal class BrowseViewModel(
    private val getGenresUseCase: GetGenresUseCase,
    private val getPlatformsUseCase: GetPlatformsUseCase,
) : BaseViewModel<BrowseUiState, UiEvent, UiAction>() {
    override fun createInitialState(): BrowseUiState = BrowseUiState()

    override fun obtainEvent(event: UiEvent) {}

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getGenresUseCase.invoke().collect { status ->
                    setState(currentState.copy(genres = status))
                }
            }
            launch {
                getPlatformsUseCase.invoke().collect { status ->
                    setState(currentState.copy(platforms = status))
                }
            }
        }
    }
}