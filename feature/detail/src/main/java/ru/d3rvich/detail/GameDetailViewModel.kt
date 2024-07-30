package ru.d3rvich.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.common.navigation.NavType
import ru.d3rvich.common.navigation.Screens
import ru.d3rvich.core.domain.model.LoadSource
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.model.Status
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.CheckIsGameFavoriteUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.core.domain.entities.GameDetailEntity
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
    private val getGameDetailUseCase: Provider<GetGameDetailUseCase>,
    private val getScreenshotsUseCase: Provider<GetScreenshotsUseCase>,
    private val addToFavoritesUseCase: Provider<AddToFavoritesUseCase>,
    private val removeFromFavoritesUseCase: Provider<RemoveFromFavoritesUseCase>,
    private val checkIsGameFavoriteUseCase: Provider<CheckIsGameFavoriteUseCase>,
) : BaseViewModel<GameDetailUiState, GameDetailUiEvent, UiAction>() {
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

    private val args: Args = extractArgs(savedStateHandle)

    init {
        load(args)
    }

    private fun extractArgs(savedStateHandle: SavedStateHandle): Args {
        return savedStateHandle.toRoute<Screens.GameDetail>(
            typeMap = mapOf(typeOf<LoadSource>() to LoadSource.NavType)
        ).run {
            Args(gameId, loadSource)
        }
    }

    private fun load(args: Args) {
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

            else -> unexpectedEventError(event, state)
        }
    }

    private fun reduce(state: GameDetailUiState.Error, event: GameDetailUiEvent) {
        when (event) {
            GameDetailUiEvent.OnRefresh -> {
                load(args)
            }

            else -> unexpectedEventError(event, state)
        }
    }
}

private data class Args(val gameId: Int, val loadSource: LoadSource)