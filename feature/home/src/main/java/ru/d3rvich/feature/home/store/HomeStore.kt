package ru.d3rvich.feature.home.store

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.entities.GameEntity

internal interface HomeStore : Store<HomeStore.Intent, HomeStore.State, Nothing> {
    sealed interface Intent {
        data object OnRefresh : Intent
        class OnSearchChange(val searchText: String) : Intent
    }

    @Stable
    data class State(
        val games: Flow<PagingData<GameEntity>>,
        val search: String = "",
        val isFilterEdited: Boolean = false,
    )
}