package ru.d3rvich.feature.home.api

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.ui.model.GameUiModel

@Stable
interface HomeComponent {
    val games: Flow<PagingData<GameUiModel>>

    val model: StateFlow<Model>

    fun openGameDetail(gameId: Int)
    fun openSettings()
    fun openFilter()

    fun setSearch(text: String)
    fun setListVDisplayOption(listDisplayOption: ListDisplayOption)

    @Immutable
    data class Model(
        val search: String,
        val isFilterEdited: Boolean,
        val listDisplayOption: ListDisplayOption
    )

    sealed interface Output {
        data object OpenSettings : Output
        data object OpenFilter : Output
        data class OpenGameDetail(val gameId: Int) : Output
    }

    interface Factory {
        fun create(componentContext: ComponentContext, output: (Output) -> Unit): HomeComponent
    }
}