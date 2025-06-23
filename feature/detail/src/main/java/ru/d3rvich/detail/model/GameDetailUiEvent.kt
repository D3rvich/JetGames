package ru.d3rvich.detail.model

import ru.d3rvich.core.ui.base.UiEvent

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
internal sealed interface GameDetailUiEvent : UiEvent {
    data object OnRefresh : GameDetailUiEvent
    data class OnFavoriteChange(val isFavorite: Boolean) : GameDetailUiEvent
    data class OnGameStoreSelected(val storeId: Int) : GameDetailUiEvent
}