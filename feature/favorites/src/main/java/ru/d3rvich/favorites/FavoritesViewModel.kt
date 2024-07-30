package ru.d3rvich.favorites

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.core.ui.base.UiEvent
import ru.d3rvich.core.domain.usecases.GetFavoriteGamesUseCase
import ru.d3rvich.favorites.model.FavoritesUiState
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@HiltViewModel
internal class FavoritesViewModel @Inject constructor(private val getFavoriteGamesUseCase: Provider<GetFavoriteGamesUseCase>) :
    BaseViewModel<FavoritesUiState, UiEvent, UiAction>() {
    override fun createInitialState(): FavoritesUiState = FavoritesUiState(emptyFlow())

    override fun obtainEvent(event: UiEvent) {

    }

    init {
        load()
    }

    private fun load() {
        setState(
            FavoritesUiState(
                games = getFavoriteGamesUseCase.get().invoke("").cachedIn(viewModelScope)
            )
        )
    }
}