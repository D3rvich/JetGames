package ru.d3rvich.detail.model

import ru.d3rvich.core.ui.base.UiAction

internal sealed interface GameDetailUiAction : UiAction {
    data object ShowGameStoreDownloadError : GameDetailUiAction
}