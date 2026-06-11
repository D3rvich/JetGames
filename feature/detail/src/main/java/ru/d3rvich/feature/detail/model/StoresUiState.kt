package ru.d3rvich.feature.detail.model

import androidx.compose.runtime.Immutable
import ru.d3rvich.core.domain.entities.StoreEntity

@Immutable
internal sealed interface StoresUiState {

    data object Loading: StoresUiState

    data object Empty: StoresUiState

    data class Success(val stores: List<StoreEntity>): StoresUiState

    data class Error(val throwable: Throwable): StoresUiState
}