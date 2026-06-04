package ru.d3rvich.feature.detail

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.StoreEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.GetStoreLinksByGameIdUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.feature.detail.browser.BrowserManager
import ru.d3rvich.feature.detail.model.GameDetailUiAction
import ru.d3rvich.feature.detail.model.GameDetailUiEvent
import ru.d3rvich.feature.detail.model.GameDetailUiModel
import ru.d3rvich.feature.detail.model.GameDetailUiState
import ru.d3rvich.feature.detail.model.ScreenshotsUiState
import ru.d3rvich.feature.detail.model.StoresUiModel
import ru.d3rvich.feature.detail.model.StoresUiState
import ru.d3rvich.feature.detail.model.toGameDetailEntity
import ru.d3rvich.feature.detail.model.toGameDetailUiModel

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

    private val browserManager = BrowserManager(context) // TODO: Получать из di

    private fun loadGameDetail(gameId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getGameDetailUseCase.invoke(gameId).collect { status ->
                when (status) {
                    LoadingResult.Loading -> setState(GameDetailUiState.Loading)
                    is LoadingResult.Success -> {
                        val uiModel = status.value.toGameDetailUiModel()
                        val detail = GameDetailUiState.Detail(
                            gameDetail = uiModel,
                            screenshots = ScreenshotsUiState.Loading,
                            stores = StoresUiState.Loading
                        )
                        setState(detail)
                        when (uiModel.storesUiModel) {
                            is StoresUiModel.EmptyUrls -> fetchStoreLinks(uiModel)
                            StoresUiModel.Empty -> setState(detail.copy(stores = StoresUiState.Empty))
                            is StoresUiModel.Full -> setState(
                                detail.copy(stores = StoresUiState.Success(stores = uiModel.storesUiModel.stores))
                            )
                        }
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

    private suspend fun fetchStoreLinks(gameDetail: GameDetailUiModel) {
        fun applyStoresUiStateToDetail(
            uiState: StoresUiState,
            gameDetail: GameDetailUiModel? = null
        ) {
            (currentState as? GameDetailUiState.Detail)?.let { detail ->
                setState(
                    detail.copy(stores = uiState, gameDetail = gameDetail ?: detail.gameDetail)
                )
            }
        }
        applyStoresUiStateToDetail(StoresUiState.Loading)
        withContext(Dispatchers.IO) {
            when (val result = getStoreLinksUseCase.invoke(gameId)) {
                is Result.Failure -> applyStoresUiStateToDetail(StoresUiState.Error(result.throwable))
                is Result.Success -> {
                    withContext(Dispatchers.Default) {
                        val updatedStores = mutableListOf<StoreEntity>()
                        val linkByStoreId = result.value.associateBy { it.storeId }
                        gameDetail.storesUiModel.stores.forEach { store ->
                            linkByStoreId[store.id]?.let { storeLink ->
                                updatedStores.add(store.copy(url = storeLink.url))
                            }
                        }
                        val updatedGameDetail =
                            gameDetail.copy(storesUiModel = StoresUiModel.Full(updatedStores.toPersistentList()))
                        applyStoresUiStateToDetail(
                            uiState = StoresUiState.Success(stores = updatedStores),
                            gameDetail = updatedGameDetail
                        )
                        if (updatedGameDetail.isFavorite) {
                            addToFavoritesUseCase(updatedGameDetail.toGameDetailEntity())
                        }
                    }
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
                        when (val result = getScreenshotsUseCase.invoke(gameId = gameDetail.id)) {
                            is Result.Success -> {
                                setState(
                                    state.copy(
                                        screenshots = ScreenshotsUiState.Success(result.value),
                                        gameDetail = state.gameDetail.copy(screenshots = result.value.toPersistentList())
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
                viewModelScope.launch(Dispatchers.IO) {
                    if (event.isFavorite) {
                        addToFavoritesUseCase.invoke(state.gameDetail.toGameDetailEntity())
                    } else {
                        removeFromFavoritesUseCase.invoke(state.gameDetail.toGameDetailEntity())
                    }
                    setState(state.copy(gameDetail = state.gameDetail.copy(isFavorite = event.isFavorite)))
                }
            }

            is GameDetailUiEvent.OnGameStoreSelected -> {
                browserManager.launchUrl(event.url.toUri())
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