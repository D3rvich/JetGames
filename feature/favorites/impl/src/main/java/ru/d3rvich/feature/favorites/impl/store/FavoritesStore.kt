package ru.d3rvich.feature.favorites.impl.store

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.entity.GameEntity

internal interface FavoritesStore : Store<Nothing, FavoritesStore.State, Nothing> {
    @Immutable
    data class State(val games: Flow<PagingData<GameEntity>>)
}