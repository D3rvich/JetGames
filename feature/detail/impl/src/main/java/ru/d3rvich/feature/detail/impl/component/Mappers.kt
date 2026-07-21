package ru.d3rvich.feature.detail.impl.component

import kotlinx.collections.immutable.toImmutableList
import ru.d3rvich.core.model.Result
import ru.d3rvich.core.model.map
import ru.d3rvich.feature.detail.api.LoadingModel
import ru.d3rvich.feature.detail.api.GameDetailComponent
import ru.d3rvich.feature.detail.impl.store.GameDetailStore

internal val stateToModel: (Result<GameDetailStore.State>) -> GameDetailComponent.Model =
    { result ->
        when (result) {
            Result.Loading -> GameDetailComponent.Model.Loading
            is Result.Error -> GameDetailComponent.Model.Error(result.throwable)
            is Result.Success -> {
                val screenshots =
                    result.value.screenshots.map { it.toImmutableList() }.toLoadingModel()
                val stores = result.value.stores.map { it.toImmutableList() }.toLoadingModel()
                GameDetailComponent.Model.Content(result.value.gameDetail, screenshots, stores)
            }
        }
    }

private fun <T> Result<T>.toLoadingModel(): LoadingModel<T> = when (this) {
    is Result.Loading -> LoadingModel.Loading
    is Result.Success -> LoadingModel.Success(this.value)
    is Result.Error -> LoadingModel.Error(this.throwable)
}