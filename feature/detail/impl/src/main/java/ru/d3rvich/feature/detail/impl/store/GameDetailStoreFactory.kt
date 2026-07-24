package ru.d3rvich.feature.detail.impl.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.usecases.AddToFavoritesUseCase
import ru.d3rvich.core.domain.usecases.GetGameDetailUseCase
import ru.d3rvich.core.domain.usecases.GetScreenshotsUseCase
import ru.d3rvich.core.domain.usecases.GetStoreLinksByGameIdUseCase
import ru.d3rvich.core.domain.usecases.RemoveFromFavoritesUseCase
import ru.d3rvich.core.entity.GameDetailEntity
import ru.d3rvich.core.entity.ScreenshotEntity
import ru.d3rvich.core.entity.StoreEntity
import ru.d3rvich.core.model.Result

@Factory
internal class GameDetailStoreFactory(
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getScreenshotsUseCase: GetScreenshotsUseCase,
    private val getStoreLinksUseCase: GetStoreLinksByGameIdUseCase,
    private val storeFactory: StoreFactory = DefaultStoreFactory()
) {
    fun create(gameId: Int): GameDetailStore = object : GameDetailStore,
        Store<GameDetailStore.Intent, Result<GameDetailStore.State>, Nothing> by storeFactory.create<GameDetailStore.Intent, Action, Msg, Result<GameDetailStore.State>, Nothing>(
            name = "GameDetailStore",
            initialState = Result.Loading,
            bootstrapper = SimpleBootstrapper(Action.LoadGameDetail),
            executorFactory = coroutineExecutorFactory {
                onAction<Action.LoadGameDetail> {
                    getGameDetailUseCase.invoke(gameId).map { result ->
                        when (result) {
                            Result.Loading -> Msg.ShowLoading
                            is Result.Error -> Msg.ShowError(result.throwable)

                            is Result.Success -> {
                                Msg.ShowGameDetail(result.value)
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
                            dispatch(Msg.UpdateScreenshots(Result.Success(emptyList())))

                        action.gameDetail.screenshots.isNotEmpty() ->
                            dispatch(Msg.UpdateScreenshots(Result.Success(action.gameDetail.screenshots.toImmutableList())))

                        else -> {
                            flow { emit(getScreenshotsUseCase(gameId)) }
                                .map { result ->
                                    when (result) {
                                        is Result.Success -> {
                                            if (action.gameDetail.isFavorite) {
                                                addToFavoritesUseCase(
                                                    action.gameDetail
                                                        .copy(screenshots = result.value)
                                                )
                                            }
                                            Result.Success(result.value.toImmutableList())
                                        }

                                        else -> result
                                    }
                                }
                                .map { result -> Msg.UpdateScreenshots(result) }
                                .flowOn(Dispatchers.Default)
                                .onEach { msg -> dispatch(msg) }
                                .launchIn(this)
                        }
                    }
                }
                onAction<Action.LoadStores> { action ->
                    val stores = action.gameDetail.stores
                    when {
                        stores.isEmpty() -> dispatch(Msg.UpdateStoreLinks(Result.Success(emptyList())))

                        stores.any { it.url == null } -> {
                            flow { emit(getStoreLinksUseCase(gameId)) }
                                .map { result ->
                                    when (result) {
                                        is Result.Success -> {
                                            val linkByStoreId =
                                                result.value.associateBy { it.storeId }
                                            val updatedStores =
                                                action.gameDetail.stores.mapNotNull { store ->
                                                    linkByStoreId[store.id]?.let { storeLink ->
                                                        store.copy(url = storeLink.url)
                                                    }
                                                }.toImmutableList()
                                            if (action.gameDetail.isFavorite) {
                                                val updatedGameDetail = action.gameDetail.copy(
                                                    stores = updatedStores
                                                )
                                                addToFavoritesUseCase.invoke(updatedGameDetail)
                                            }
                                            Result.Success(updatedStores)
                                        }

                                        is Result.Error -> Result.Error(result.throwable)
                                        Result.Loading -> Result.Loading
                                    }
                                }
                                .map { result ->
                                    Msg.UpdateStoreLinks(result)
                                }
                                .flowOn(Dispatchers.Default)
                                .onEach { msg -> dispatch(msg) }
                                .launchIn(this)
                        }

                        // All stores has ulr
                        else -> dispatch(
                            Msg.UpdateStoreLinks(
                                Result.Success(action.gameDetail.stores)
                            )
                        )
                    }
                }

                onIntent<GameDetailStore.Intent.OnRefresh> { forward(Action.LoadGameDetail) }
                onIntent<GameDetailStore.Intent.OnFavoriteChange> { intent ->
                    dispatch(Msg.UpdateFavorite(intent.isFavorite))
                    val gameDetail =
                        (state() as? Result.Success<GameDetailStore.State>)?.value?.gameDetail
                            ?: return@onIntent
                    launch(Dispatchers.Default) {
                        if (intent.isFavorite) {
                            addToFavoritesUseCase(gameDetail)
                        } else {
                            removeFromFavoritesUseCase(gameDetail)
                        }
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    Msg.ShowLoading -> Result.Loading
                    is Msg.ShowError -> Result.Error(msg.throwable)
                    is Msg.ShowGameDetail -> Result.Success(GameDetailStore.State(msg.gameDetail))
                    is Msg.UpdateFavorite -> if (this is Result.Success) {
                        val updated = this.value.gameDetail.copy(isFavorite = msg.isFavorite)
                        val state = this.value.copy(gameDetail = updated)
                        copy(value = state)
                    } else this

                    is Msg.UpdateScreenshots -> if (this is Result.Success) {
                        val state = this.value.copy(screenshots = msg.result)
                        copy(value = state)
                    } else this

                    is Msg.UpdateStoreLinks -> if (this is Result.Success) {
                        val state = this.value.copy(stores = msg.result)
                        copy(value = state)
                    } else this
                }
            }
        ) {}

    private sealed interface Action {
        data object LoadGameDetail : Action
        data class LoadScreenshots(val gameDetail: GameDetailEntity) : Action

        data class LoadStores(val gameDetail: GameDetailEntity) : Action
    }

    private sealed interface Msg {
        data object ShowLoading : Msg
        data class ShowError(val throwable: Throwable) : Msg
        data class ShowGameDetail(val gameDetail: GameDetailEntity) : Msg
        data class UpdateFavorite(val isFavorite: Boolean) : Msg
        data class UpdateScreenshots(val result: Result<List<ScreenshotEntity>>) : Msg
        data class UpdateStoreLinks(val result: Result<List<StoreEntity>>) : Msg
    }
}