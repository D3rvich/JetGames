package ru.d3rvich.feature.detail.impl.store

import com.arkivanov.mvikotlin.core.store.Store
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.entity.StoreEntity
import ru.d3rvich.core.model.Result

internal interface GameDetailStore :
    Store<GameDetailStore.Intent, Result<GameDetailStore.State>, Nothing> {

    sealed interface Intent {
        data object OnRefresh : Intent
        data class OnFavoriteChange(val isFavorite: Boolean) : Intent
    }

    data class State(
        val gameDetail: GameDetailEntity,
        val screenshots: Result<List<ScreenshotEntity>> = Result.Loading,
        val stores: Result<List<StoreEntity>> = Result.Loading
    )
}