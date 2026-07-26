package ru.d3rvich.feature.favorites.api

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.ui.model.GameUiModel

@Stable
interface FavoritesComponent {
    val model: Model

    fun gameClicked(gameId: Int)
    fun settingsClicked()

    @Stable
    data class Model(val games: Flow<PagingData<GameUiModel>>)

    sealed interface Output {
        data object OpenSettings : Output
        class OpenGameDetail(val gameId: Int) : Output
    }

    interface Factory {
        fun create(componentContext: ComponentContext, output: (Output) -> Unit): FavoritesComponent
    }
}