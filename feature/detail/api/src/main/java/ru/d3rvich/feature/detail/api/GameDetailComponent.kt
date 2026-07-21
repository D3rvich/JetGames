package ru.d3rvich.feature.detail.api

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.entity.StoreEntity

@Stable
interface GameDetailComponent {
    val gameId: Int

    val model: Value<Model>

    fun refresh()
    fun setFavorites(isFavorite: Boolean)

    fun openScreenshots(selectedItem: Int, items: List<ScreenshotEntity>)
    fun openGameStore(url: String)
    fun close()

    @Immutable
    sealed interface Model {
        data object Loading : Model
        data class Error(val throwable: Throwable) : Model

        @Stable
        data class Content(
            val gameDetail: GameDetailEntity,
            val screenshots: LoadingModel<ImmutableList<ScreenshotEntity>>,
            val stores: LoadingModel<ImmutableList<StoreEntity>>
        ) : Model
    }

    sealed interface Output {
        data object Finished : Output
        data class OpenScreenshots(val selectedItem: Int, val items: List<ScreenshotEntity>) :
            Output
    }

    interface Factory {
        fun create(
            context: ComponentContext,
            gameId: Int,
            output: (Output) -> Unit
        ): GameDetailComponent
    }
}

@Immutable
sealed interface LoadingModel<out T> {
    data object Loading : LoadingModel<Nothing>
    data class Success<out T>(val value: T) : LoadingModel<T>
    data class Error(val throwable: Throwable) : LoadingModel<Nothing>
}