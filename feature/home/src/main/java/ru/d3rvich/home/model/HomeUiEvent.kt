package ru.d3rvich.home.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.ui.base.UiEvent

/**
 * Created by Ilya Deryabin at 02.02.2024
 */
@Immutable
internal sealed interface HomeUiEvent : UiEvent {
    data object OnRefresh : HomeUiEvent
    class OnSearchChange(val textSearch: String) : HomeUiEvent
}