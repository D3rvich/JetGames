package ru.d3rvich.feature.home.impl.store

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.model.ListDisplayOption

internal interface HomeStore : Store<HomeStore.Intent, HomeStore.State, Nothing> {
    sealed interface Intent {
        data object Refresh : Intent
        data class SearchChange(val searchText: String) : Intent
        data class ListDisplayChange(val listDisplayOption: ListDisplayOption): Intent
    }

    sealed interface State {
        data object Loading : State

        @Stable
        data class Content(
            val games: Flow<PagingData<GameEntity>>,
            val search: String,
            val isFilterEdited: Boolean,
            val listDisplayOption: ListDisplayOption,
        ) : State
    }
}