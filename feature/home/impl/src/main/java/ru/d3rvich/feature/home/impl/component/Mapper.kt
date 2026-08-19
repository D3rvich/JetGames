package ru.d3rvich.feature.home.impl.component

import androidx.paging.map
import kotlinx.coroutines.flow.map
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.ui.model.toGameUiModel
import ru.d3rvich.feature.home.api.HomeComponent.Model
import ru.d3rvich.feature.home.impl.store.HomeStore

internal val stateToModel: (HomeStore.State) -> Model = { state ->
    when (state) {
        is HomeStore.State.Content -> Model.Content(
            games = state.games.map { pagingData -> pagingData.map(GameEntity::toGameUiModel) },
            search = state.search,
            isFilterEdited = state.isFilterEdited,
            listDisplayOption = state.listDisplayOption
        )

        HomeStore.State.Loading -> Model.Loading
    }
}