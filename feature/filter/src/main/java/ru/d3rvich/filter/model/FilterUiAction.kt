package ru.d3rvich.filter.model

import ru.d3rvich.core.ui.base.UiAction

/**
 * Created by Ilya Deryabin at 21.03.2024
 */
internal sealed interface FilterUiAction : UiAction {
    data object NavigateBack : FilterUiAction
}