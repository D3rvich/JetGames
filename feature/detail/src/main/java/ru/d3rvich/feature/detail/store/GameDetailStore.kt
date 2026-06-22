package ru.d3rvich.feature.detail.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import ru.d3rvich.feature.detail.model.GameDetailUiModel
import ru.d3rvich.feature.detail.model.ScreenshotsState
import ru.d3rvich.feature.detail.model.StoresState

internal interface GameDetailStore : Store<GameDetailStore.Intent, GameDetailStore.State, Nothing> {

    sealed interface Intent {
        data object OnRefresh : Intent
        data class OnFavoriteChange(val isFavorite: Boolean) : Intent
        data class OnGameStoreSelected(val url: String) : Intent
    }

    @Immutable
    sealed interface State {
        data object Loading : State
        data class GameDetail(
            val gameDetail: GameDetailUiModel,
            val screenshots: ScreenshotsState = ScreenshotsState.Loading,
            val stores: StoresState = StoresState.Loading
        ) : State

        data class Error(val errorMessage: String) : State
    }
}