package ru.d3rvich.feature.favorites

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.usecases.GetFavoriteGamesUseCase
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.core.ui.base.UiEvent
import ru.d3rvich.feature.favorites.model.FavoritesUiState

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@KoinViewModel
internal class FavoritesViewModel(private val getFavoriteGamesUseCase: GetFavoriteGamesUseCase) :
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
                games = getFavoriteGamesUseCase.invoke("").cachedIn(viewModelScope)
            )
        )
    }
}