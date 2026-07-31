package ru.d3rvich.feature.favorites.impl.component

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.domain.usecases.GetFavoriteGamesUseCase
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.ui.model.GameUiModel
import ru.d3rvich.core.ui.model.toGameUiModel
import ru.d3rvich.feature.favorites.api.FavoritesComponent

@Factory(binds = [DefaultFavoritesComponent::class])
internal class DefaultFavoritesComponent(
    @InjectedParam componentContext: ComponentContext,
    @InjectedParam private val output: (FavoritesComponent.Output) -> Unit,
    getFavoriteGamesUseCase: GetFavoriteGamesUseCase
) : FavoritesComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    override val games: Flow<PagingData<GameUiModel>> =
        getFavoriteGamesUseCase.invoke("").map { pagingData ->
            pagingData.map(GameEntity::toGameUiModel)
        }.cachedIn(coroutineScope)

    override fun gameClicked(gameId: Int) {
        output(FavoritesComponent.Output.OpenGameDetail(gameId))
    }

    override fun settingsClicked() {
        output(FavoritesComponent.Output.OpenSettings)
    }
}