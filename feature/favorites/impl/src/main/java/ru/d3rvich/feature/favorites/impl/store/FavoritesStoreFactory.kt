package ru.d3rvich.feature.favorites.impl.store

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.annotation.Factory
import ru.d3rvich.core.domain.usecases.GetFavoriteGamesUseCase
import ru.d3rvich.core.entity.GameEntity

@Factory
internal class FavoritesStoreFactory(
    private val getFavoriteGamesUseCase: GetFavoriteGamesUseCase,
    private val storeFactory: StoreFactory = DefaultStoreFactory(),
) {
    fun create(): FavoritesStore =
        object : FavoritesStore,
            Store<Nothing, FavoritesStore.State, Nothing> by storeFactory.create<Nothing, Message, Message, FavoritesStore.State, Nothing>(
                name = "FavoritesStore",
                initialState = FavoritesStore.State(emptyFlow()),
                bootstrapper = coroutineBootstrapper {
                    val gamesFlow = getFavoriteGamesUseCase.invoke("").cachedIn(this)
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