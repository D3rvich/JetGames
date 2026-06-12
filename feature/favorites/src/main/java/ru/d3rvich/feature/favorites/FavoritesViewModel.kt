package ru.d3rvich.feature.favorites

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.SharingStarted
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.usecases.GetFavoriteGamesUseCase
import ru.d3rvich.feature.favorites.store.FavoritesStoreFactory

/**
 * Created by Ilya Deryabin at 26.04.2024
 */
@Stable
@KoinViewModel
class FavoritesViewModel(
    getFavoriteGamesUseCase: GetFavoriteGamesUseCase,
    storeFactory: StoreFactory = DefaultStoreFactory(),
) : ViewModel() {
    private val store =
        FavoritesStoreFactory(storeFactory, getFavoriteGamesUseCase, viewModelScope).create()

    internal val uiState = store.stateFlow(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}