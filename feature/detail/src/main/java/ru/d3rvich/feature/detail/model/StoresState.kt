package ru.d3rvich.feature.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.StoreEntity

@Immutable
internal sealed interface StoresState {

    data object Loading: StoresState

    data object Empty: StoresState

    data class Success(val stores: ImmutableList<StoreEntity>): StoresState

    data class Error(val throwable: Throwable): StoresState
}