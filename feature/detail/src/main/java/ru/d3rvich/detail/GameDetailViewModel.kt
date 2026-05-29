package ru.d3rvich.detail

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.StoreEntity
import ru.d3rvich.core.domain.entities.StoreLinkEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.GetStoreLinksByGameIdUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.detail.browser.BrowserManager
import ru.d3rvich.detail.model.GameDetailUiAction
import ru.d3rvich.detail.model.GameDetailUiEvent
import ru.d3rvich.detail.model.GameDetailUiState
import ru.d3rvich.detail.model.ScreenshotsUiState

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@KoinViewModel
internal class GameDetailViewModel(
    context: Context,
    @InjectedParam private val gameId: Int,
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val getScreenshotsUseCase: GetScreenshotsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getStoreLinksUseCase: GetStoreLinksByGameIdUseCase
) : BaseViewModel<GameDetailUiState, GameDetailUiEvent, GameDetailUiAction>() {
    override fun createInitialState(): GameDetailUiState = GameDetailUiState.Loading

    init {
        loadGameDetail(gameId)
    }

    override fun obtainEvent(event: GameDetailUiEvent) {
        when (val state = currentState) {
            is GameDetailUiState.Detail -> {
                reduce(state, event)
            }

            is GameDetailUiState.Error -> {
                reduce(state, event)
            }

            else -> unexpectedEventError(event)
        }
    }

    private val browserManager = BrowserManager(context)

    private var gameStoreLinks: List<StoreLinkEntity> = emptyList()

    private fun loadGameDetail(gameId: Int) {
        viewModelScope.launch {
            getGameDetailUseCase.invoke(gameId).collect { status ->
                when (status) {
                    LoadingResult.Loading -> setState(GameDetailUiState.Loading)
                    is LoadingResult.Success -> {
                        val stores = status.value.stores
                        if (stores.isNotEmpty()) {
                            loadLinks(gameId)
                            if (status.value.isFavorite && gameStoreLinks.isNotEmpty()) {
                                addToFavoritesUseCase.invoke(
                                    status.value.copy(
                                        stores = uniteStoresWithLinks(
                                            stores = stores,
                                            links = gameStoreLinks
                                        )
                                    )
                                )
                            }
                        }
                        setState(
                            GameDetailUiState.Detail(
                                gameDetail = status.value,
                                screenshots = ScreenshotsUiState.NoScreenshots,
                            )
                        )
                        if (status.value.screenshotCount > 0) {
                            getScreenshots(gameDetail = status.value)
                        }
                    }

                    is LoadingResult.Error -> setState(
                        GameDetailUiState.Error(
                            status.throwable.localizedMessage ?: "error"
                        )
                    )
                }
            }
        }
    }

    private fun loadLinks(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getStoreLinksUseCase.invoke(gameId = gameId)) {
                is Result.Success -> {
                    gameStoreLinks = result.value
                }

                is Result.Failure -> {
                    sendAction { GameDetailUiAction.ShowGameStoreDownloadError }
                }
            }
        }
    }

    private suspend fun getScreenshots(gameDetail: GameDetailEntity) {
        when (val state = currentState) {
            is GameDetailUiState.Detail -> {
                withContext(Dispatchers.IO) {
                    if (gameDetail.screenshots.isNotEmpty()) {
                        setState(state.copy(screenshots = ScreenshotsUiState.Success(gameDetail.screenshots)))
                    } else {
                        setState(state.copy(screenshots = ScreenshotsUiState.Loading))
                        when (val result =
                            getScreenshotsUseCase.invoke(gameId = gameDetail.id)) {
                            is Result.Success -> {
                                setState(
                                    state.copy(
                                        screenshots = ScreenshotsUiState.Success(result.value),
                                        gameDetail = state.gameDetail.copy(screenshots = result.value)
                                    )
                                )
                            }

                            is Result.Failure -> {
                                setState(state.copy(screenshots = ScreenshotsUiState.Error(result.throwable)))
                            }
                        }
                    }
                }
            }

            else -> error("Unexpected method call for current state: $currentState")
        }
    }

    private fun reduce(state: GameDetailUiState.Detail, event: GameDetailUiEvent) {
        when (event) {
            is GameDetailUiEvent.OnFavoriteChange -> {
                viewModelScope.launch {
                    if (event.isFavorite) {
                        val gameDetail: GameDetailEntity = if (gameStoreLinks.isNotEmpty()) {
                            val storesWithUrls: MutableList<StoreEntity> = mutableListOf()
                            state.gameDetail.stores.forEach { store ->
                                gameStoreLinks.find { it.storeId == store.id }?.let {
                                    storesWithUrls.add(store.copy(url = it.url))
                                }
                            }
                            state.gameDetail.copy(stores = storesWithUrls)
                        } else {
                            state.gameDetail
                        }
                        addToFavoritesUseCase.invoke(gameDetail)
                    } else {
                        removeFromFavoritesUseCase.invoke(state.gameDetail)
                    }
                    setState(state.copy(gameDetail = state.gameDetail.copy(isFavorite = event.isFavorite)))
                }
            }

            is GameDetailUiEvent.OnGameStoreSelected -> {
                if (gameStoreLinks.isNotEmpty()) {
                    val storeUrl = gameStoreLinks.find { it.storeId == event.storeId }?.url!!
                    browserManager.launchUrl(storeUrl.toUri())
                }
            }

            else -> unexpectedEventError(event, state)
        }
    }

    private fun reduce(state: GameDetailUiState.Error, event: GameDetailUiEvent) {
        when (event) {
            GameDetailUiEvent.OnRefresh -> {
                loadGameDetail(gameId)
            }

            else -> unexpectedEventError(event, state)
        }
    }
}

private fun uniteStoresWithLinks(
    stores: List<StoreEntity>,
    links: List<StoreLinkEntity>
): List<StoreEntity> {
    val result = mutableListOf<StoreEntity>()
    links.forEach { link ->
        stores.find { store -> store.id == link.storeId }!!.let { result.add(it) }
    }
    return result
}