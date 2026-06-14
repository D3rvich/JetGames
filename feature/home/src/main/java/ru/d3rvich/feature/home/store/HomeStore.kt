package ru.d3rvich.feature.home.store

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.feature.home.model.ListDisplayMode

internal interface HomeStore : Store<HomeStore.Intent, HomeStore.State, Nothing> {
    sealed interface Intent {
        data object Refresh : Intent
        data class SearchChange(val searchText: String) : Intent
        data class ListDisplayChange(val listDisplayMode: ListDisplayMode): Intent
    }

    sealed interface State {
        data object Loading : State

        @Stable
        data class Content(
            val games: Flow<PagingData<GameEntity>>,
            val search: String,
            val isFilterEdited: Boolean,
            val listDisplayMode: ListDisplayMode,
        ) : State
    }
}