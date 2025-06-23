package ru.d3rvich.detail

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.d3rvich.common.navigation.NavType
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.core.domain.entities.GameDetailEntity
import ru.d3rvich.core.domain.entities.GameStoreEntity
import ru.d3rvich.core.domain.model.LoadSource
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.CheckIsGameFavoriteUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetGameStoresByIdUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.detail.browser.BrowserManager
import ru.d3rvich.detail.model.GameDetailUiAction
import ru.d3rvich.detail.model.GameDetailUiEvent
import ru.d3rvich.detail.model.GameDetailUiState
import ru.d3rvich.detail.model.ScreenshotsUiState
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.typeOf

/**
 * Created by Ilya Deryabin at 24.02.2024
 */
@HiltViewModel
internal class GameDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getGameDetailUseCase: Provider<GetGameDetailUseCase>,
    private val getScreenshotsUseCase: Provider<GetScreenshotsUseCase>,
    private val addToFavoritesUseCase: Provider<AddToFavoritesUseCase>,
    private val removeFromFavoritesUseCase: Provider<RemoveFromFavoritesUseCase>,
    private val checkIsGameFavoriteUseCase: Provider<CheckIsGameFavoriteUseCase>,
    private val getGamesStoresUseCase: Provider<GetGameStoresByIdUseCase>
) : BaseViewModel<GameDetailUiState, GameDetailUiEvent, GameDetailUiAction>() {
    override fun createInitialState(): GameDetailUiState = GameDetailUiState.Loading

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

    private var gameStores: List<GameStoreEntity>? = null

    private val args: Args = extractArgs(savedStateHandle).also { args ->
        loadGameDetail(args)
        loadStores(args.gameId)
    }

    private fun extractArgs(savedStateHandle: SavedStateHandle): Args {
        return savedStateHandle.toRoute<Screens.GameDetail>(
            typeMap = mapOf(typeOf<LoadSource>() to LoadSource.NavType)
        ).run {
            Args(gameId, loadSource)
        }
    }

    private fun loadGameDetail(args: Args) {
        viewModelScope.launch {
            val isFavorite = async { checkIsGameFavoriteUseCase.get().invoke(args.gameId) }
            getGameDetailUseCase.get().invoke(args.gameId, args.loadSource).collect { status ->
                when (status) {
                    Status.Loading -> setState(GameDetailUiState.Loading)
                    is Status.Success -> {
                        setState(
                            GameDetailUiState.Detail(
                                gameDetail = status.value,
                                screenshots = ScreenshotsUiState.NoScreenshots,
                                isFavorite = isFavorite.await()
                            )
                        )
                        if (status.value.screenshotCount > 0) {
                            getScreenshots(gameDetail = status.value)
                        }
                    }

                    is Status.Error -> setState(
                        GameDetailUiState.Error(
                            status.throwable.localizedMessage ?: "error"
                        )
                    )
                }
            }
        }
    }

    private fun loadStores(gameId: Int, selectedStore: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getGamesStoresUseCase.get().invoke(gameId = gameId)) {
                is Result.Success -> {
                    gameStores = result.value
                    selectedStore?.let {
                        val storeUrl = result.value.find { it.storeId == selectedStore }?.url!!
                        browserManager.launchUrl(storeUrl.toUri())
                    }
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
                            getScreenshotsUseCase.get().invoke(gameId = gameDetail.id)) {
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
                        addToFavoritesUseCase.get().invoke(state.gameDetail)
                    } else {
                        removeFromFavoritesUseCase.get().invoke(state.gameDetail)
                    }
                    setState(state.copy(isFavorite = event.isFavorite))
                }
            }

            is GameDetailUiEvent.OnGameStoreSelected -> {
                if (!gameStores.isNullOrEmpty()) {
                    val storeUrl = gameStores?.find { it.storeId == event.storeId }?.url!!
                    browserManager.launchUrl(storeUrl.toUri())
                }
            }

            else -> unexpectedEventError(event, state)
        }
    }

    private fun reduce(state: GameDetailUiState.Error, event: GameDetailUiEvent) {
        when (event) {
            GameDetailUiEvent.OnRefresh -> {
                loadGameDetail(args)
            }

            else -> unexpectedEventError(event, state)
        }
    }
}

private data class Args(val gameId: Int, val loadSource: LoadSource)