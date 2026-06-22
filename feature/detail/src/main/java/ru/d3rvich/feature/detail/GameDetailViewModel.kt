package ru.d3rvich.feature.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.GetStoreLinksByGameIdUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.feature.detail.browser.BrowserManager
import ru.d3rvich.feature.detail.store.GameDetailStore
import ru.d3rvich.feature.detail.store.GameDetailStoreFactory

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@Stable
@KoinViewModel
class GameDetailViewModel(
    @InjectedParam private val gameId: Int,
    private val browserManager: BrowserManager,
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val getScreenshotsUseCase: GetScreenshotsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getStoreLinksUseCase: GetStoreLinksByGameIdUseCase,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) : ViewModel() {

    private val store: GameDetailStore = GameDetailStoreFactory(
        gameId = gameId,
        storeFactory = storeFactory,
        browserManager = browserManager,
        getGameDetailUseCase = getGameDetailUseCase,
        addToFavoritesUseCase = addToFavoritesUseCase,
        removeFromFavoritesUseCase = removeFromFavoritesUseCase,
        getScreenshotsUseCase = getScreenshotsUseCase,
        getStoreLinksUseCase = getStoreLinksUseCase,
    ).create()

    internal val uiState = store.stateFlow(viewModelScope)

    internal fun obtainEvent(intent: GameDetailStore.Intent) {
        store.accept(intent)
    }
}