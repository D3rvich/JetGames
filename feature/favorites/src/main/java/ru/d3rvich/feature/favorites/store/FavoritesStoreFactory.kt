package ru.d3rvich.feature.favorites.store

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.domain.usecases.GetFavoriteGamesUseCase

internal class FavoritesStoreFactory(
    private val storeFactory: StoreFactory,
    private val getFavoriteGamesUseCase: GetFavoriteGamesUseCase,
    private val viewModelScope: CoroutineScope
) {
    fun create(): FavoritesStore =
        object : FavoritesStore,
            Store<Nothing, FavoritesState, Nothing> by storeFactory.create<Nothing, Message, Message, FavoritesState, Nothing>(
                name = "FavoritesStore",
                initialState = FavoritesState(emptyFlow()),
                bootstrapper = coroutineBootstrapper {
                    val gamesFlow = getFavoriteGamesUseCase.invoke("").cachedIn(viewModelScope)
                    dispatch(Message.GamesLoaded(gamesFlow))
                },
                executorFactory = coroutineExecutorFactory {
                    onAction<Message.GamesLoaded> { action ->
                        dispatch(action)
                    }
                },
                reducer = { message ->
                    when (message) {
                        is Message.GamesLoaded -> copy(games = message.games)
                    }
                }
            ) {}

    private sealed interface Message {
        data class GamesLoaded(val games: Flow<PagingData<GameEntity>>) : Message
    }
}