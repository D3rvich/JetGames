package ru.d3rvich.feature.detail.store

import androidx.core.net.toUri
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.model.Result
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.GetStoreLinksByGameIdUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.feature.detail.browser.BrowserManager
import ru.d3rvich.feature.detail.model.GameDetailUiModel
import ru.d3rvich.feature.detail.model.ScreenshotsState
import ru.d3rvich.feature.detail.model.StoresState
import ru.d3rvich.feature.detail.model.StoresUiModel
import ru.d3rvich.feature.detail.model.toGameDetailEntity
import ru.d3rvich.feature.detail.model.toGameDetailUiModel

internal class GameDetailStoreFactory(
    private val gameId: Int,
    private val storeFactory: StoreFactory,
    private val browserManager: BrowserManager,
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getScreenshotsUseCase: GetScreenshotsUseCase,
    private val getStoreLinksUseCase: GetStoreLinksByGameIdUseCase
) {
    fun create(): GameDetailStore = object : GameDetailStore,
        Store<GameDetailStore.Intent, GameDetailStore.State, Nothing> by storeFactory.create<GameDetailStore.Intent, Action, Msg, GameDetailStore.State, Nothing>(
            name = "GameDetailStore",
            initialState = GameDetailStore.State.Loading,
            bootstrapper = SimpleBootstrapper(Action.LoadGameDetail),
            executorFactory = coroutineExecutorFactory {
                onAction<Action.LoadGameDetail> {
                    getGameDetailUseCase.invoke(gameId).map { loadingResult ->
                        when (loadingResult) {
                            LoadingResult.Loading -> Msg.ShowLoading
                            is LoadingResult.Error -> Msg.ShowError(
                                loadingResult.throwable.localizedMessage ?: "error"
                            )

                            is LoadingResult.Success -> {
                                val gameDetail = loadingResult.value.toGameDetailUiModel()
                                Msg.ShowGameDetail(gameDetail)
                            }
                        }
                    }.flowOn(Dispatchers.Default)
                        .onEach { msg ->
                            dispatch(msg)
                            if (msg is Msg.ShowGameDetail) {
                                forward(Action.LoadScreenshots(msg.gameDetail))
                                forward(Action.LoadStores(msg.gameDetail))
                            }
                        }
                        .launchIn(this)
                }
                onAction<Action.LoadScreenshots> { action ->
                    when {
                        action.gameDetail.screenshotCount == 0 ->
                            dispatch(Msg.UpdateScreenshots(ScreenshotsState.NoScreenshots))

                        action.gameDetail.screenshots.isNotEmpty() ->
                            dispatch(Msg.UpdateScreenshots(ScreenshotsState.Success(action.gameDetail.screenshots.toImmutableList())))

                        else -> {
                            flow { emit(getScreenshotsUseCase(gameId)) }
                                .map { result ->
                                    when (result) {
                                        is Result.Failure -> ScreenshotsState.Error(result.throwable)
                                        is Result.Success -> {
                                            if (action.gameDetail.isFavorite) {
                                                addToFavoritesUseCase(
                                                    action.gameDetail.toGameDetailEntity()
                                                        .copy(screenshots = result.value)
                                                )
                                            }
                                            ScreenshotsState.Success(result.value.toImmutableList())
                                        }
                                    }
                                }
                                .onStart { emit(ScreenshotsState.Loading) }
                                .map { state -> Msg.UpdateScreenshots(screenshotsState = state) }
                                .flowOn(Dispatchers.Default)
                                .onEach { msg -> dispatch(msg) }
                                .launchIn(this)
                        }
                    }
                }
                onAction<Action.LoadStores> { action ->
                    when (action.gameDetail.storesUiModel) {
                        StoresUiModel.Empty -> dispatch(Msg.UpdateStoreLinks(StoresState.Empty))
                        is StoresUiModel.Full -> dispatch(
                            Msg.UpdateStoreLinks(
                                StoresState.Success(action.gameDetail.storesUiModel.stores)
                            )
                        )

                        is StoresUiModel.EmptyUrls -> {
                            flow { emit(getStoreLinksUseCase(gameId)) }
                                .map { result ->
                                    when (result) {
                                        is Result.Failure -> StoresState.Error(result.throwable)
                                        is Result.Success -> {
                                            val linkByStoreId =
                                                result.value.associateBy { it.storeId }
                                            val updatedStores =
                                                action.gameDetail.storesUiModel.stores.mapNotNull { store ->
                                                    linkByStoreId[store.id]?.let { storeLink ->
                                                        store.copy(url = storeLink.url)
                                                    }
                                                }.toImmutableList()
                                            if (action.gameDetail.isFavorite) {
                                                val updatedGameDetail = action.gameDetail.copy(
                                                    storesUiModel = StoresUiModel.Full(updatedStores)
                                                )
                                                addToFavoritesUseCase.invoke(updatedGameDetail.toGameDetailEntity())
                                            }
                                            StoresState.Success(updatedStores)
                                        }
                                    }
                                }
                                .onStart { emit(StoresState.Loading) }
                                .map { state ->
                                    Msg.UpdateStoreLinks(storeState = state)
                                }
                                .flowOn(Dispatchers.Default)
                                .onEach { msg -> dispatch(msg) }
                                .launchIn(this)
                        }
                    }
                }

                onIntent<GameDetailStore.Intent.OnRefresh> { forward(Action.LoadGameDetail) }
                onIntent<GameDetailStore.Intent.OnFavoriteChange> { intent ->
                    dispatch(Msg.UpdateFavorite(intent.isFavorite))
                    val gameDetail = (state() as? GameDetailStore.State.GameDetail)?.gameDetail
                        ?: return@onIntent
                    launch(Dispatchers.Default) {
                        val gameDetailEntity = gameDetail.toGameDetailEntity()
                        if (intent.isFavorite) {
                            addToFavoritesUseCase(gameDetailEntity)
                        } else {
                            removeFromFavoritesUseCase(gameDetailEntity)
                        }
                    }
                }
                onIntent<GameDetailStore.Intent.OnGameStoreSelected> { intent ->
                    browserManager.launchUrl(intent.url.toUri())
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.ShowError -> GameDetailStore.State.Error(msg.message)
                    is Msg.ShowGameDetail -> GameDetailStore.State.GameDetail(msg.gameDetail)
                    Msg.ShowLoading -> GameDetailStore.State.Loading
                    is Msg.UpdateFavorite -> when (this) {
                        is GameDetailStore.State.GameDetail -> {
                            val updatedGameDetail =
                                gameDetail.copy(isFavorite = msg.isFavorite)
                            copy(gameDetail = updatedGameDetail)
                        }

                        else -> this
                    }

                    is Msg.UpdateScreenshots -> when (this) {
                        is GameDetailStore.State.GameDetail -> {
                            val screenshots =
                                if (msg.screenshotsState is ScreenshotsState.Success) {
                                    msg.screenshotsState.screenshots
                                } else {
                                    gameDetail.screenshots
                                }
                            copy(
                                gameDetail = gameDetail.copy(screenshots = screenshots),
                                screenshots = msg.screenshotsState
                            )
                        }

                        else -> this
                    }

                    is Msg.UpdateStoreLinks -> when (this) {
                        is GameDetailStore.State.GameDetail -> {
                            val storesUiModel = if (msg.storeState is StoresState.Success) {
                                StoresUiModel.Full(msg.storeState.stores)
                            } else {
                                gameDetail.storesUiModel
                            }
                            copy(
                                gameDetail = gameDetail.copy(storesUiModel = storesUiModel),
                                stores = msg.storeState
                            )
                        }

                        else -> this
                    }
                }
            }
        ) {}

    private sealed interface Action {
        data object LoadGameDetail : Action
        data class LoadScreenshots(val gameDetail: GameDetailUiModel) : Action

        data class LoadStores(val gameDetail: GameDetailUiModel) : Action
    }

    private sealed interface Msg {
        data object ShowLoading : Msg
        data class ShowError(val message: String) : Msg
        data class ShowGameDetail(val gameDetail: GameDetailUiModel) : Msg
        data class UpdateFavorite(val isFavorite: Boolean) : Msg
        data class UpdateScreenshots(val screenshotsState: ScreenshotsState) : Msg
        data class UpdateStoreLinks(val storeState: StoresState) : Msg
    }
}