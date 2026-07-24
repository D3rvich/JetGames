package ru.d3rvich.feature.home.api

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.model.ListDisplayOption

@Stable
interface HomeComponent {
    val model: Value<Model>

    fun openGameDetail(gameId: Int)
    fun openSettings()
    fun openFilter()

    fun refresh()
    fun setSearch(text: String)
    fun setListVDisplayOption(listDisplayOption: ListDisplayOption)

    @Immutable
    sealed interface Model {
        @Immutable
        data object Loading : Model

        @Stable
        data class Content(
            val games: Flow<PagingData<GameEntity>>,
            val search: String,
            val isFilterEdited: Boolean,
            val listDisplayOption: ListDisplayOption
        ) : Model
    }

    sealed interface Output {
        data object OpenSettings : Output
        data object OpenFilter : Output
        data class OpenGameDetail(val gameId: Int) : Output
    }

    interface Factory {
        fun create(componentContext: ComponentContext, output: (Output) -> Unit): HomeComponent
    }
}