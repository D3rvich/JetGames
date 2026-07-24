package ru.d3rvich.feature.home.impl.component

import ru.d3rvich.feature.home.api.HomeComponent.Model
import ru.d3rvich.feature.home.impl.store.HomeStore

internal val stateToModel: (HomeStore.State) -> Model = { state ->
    when (state) {
        is HomeStore.State.Content -> Model.Content(
            state.games,
            state.search,
            state.isFilterEdited,
            state.listDisplayOption
        )

        HomeStore.State.Loading -> Model.Loading
    }
}