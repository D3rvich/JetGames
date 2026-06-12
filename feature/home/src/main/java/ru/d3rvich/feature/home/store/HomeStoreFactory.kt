package ru.d3rvich.feature.home.store

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.preferences.isDefault
import ru.d3rvich.core.domain.usecases.GetGamesUseCase
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class HomeStoreFactory(
    private val storeFactory: StoreFactory,
    private val getGamesUseCase: GetGamesUseCase,
    private val filterPreferences: FilterPreferences,
    private val scope: CoroutineScope,
) {
    private val searchFlow = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    fun create(): HomeStore = object : HomeStore,
        Store<HomeStore.Intent, HomeStore.State, Nothing> by storeFactory.create<HomeStore.Intent, Action, Message, HomeStore.State, Nothing>(
            name = "HomeStore",
            initialState = HomeStore.State(games = emptyFlow()),
            bootstrapper = SimpleBootstrapper(Action.Init),
            executorFactory = coroutineExecutorFactory {
                val debounceSearchFlow = MutableStateFlow("")
                val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
                onAction<Action.Init> {
                    searchFlow
                        .debounce(SEARCH_TIMEOUT_MILLIS.toDuration(DurationUnit.MILLISECONDS))
                        .onEach { debounceSearchFlow.value = it }
                        .launchIn(scope)
                    combine(
                        debounceSearchFlow,
                        filterPreferences.filterPreferencesFlow,
                        refreshTrigger.onStart { emit(Unit) }
                    ) { search, filterPreferencesBody, _ ->
                        search to filterPreferencesBody
                    }
                        .onEach { (text, body) ->
                            val games = getGamesUseCase.invoke(text, body).cachedIn(scope)
                            val isFilterEdited = !body.isDefault()
                            dispatch(Message.GamesLoaded(games, text, isFilterEdited))
                        }.launchIn(scope)
                }

                onIntent<HomeStore.Intent.OnSearchChange> { intent ->
                    searchFlow.value = intent.searchText
                    dispatch(Message.SearchChanged(intent.searchText))
                }
                onIntent<HomeStore.Intent.OnRefresh> {
                    refreshTrigger.tryEmit(Unit)
                }
            },
            reducer = { message ->
                when (message) {
                    is Message.GamesLoaded -> copy(
                        games = message.games,
                        search = message.search,
                        isFilterEdited = message.isFilterEdited
                    )

                    is Message.SearchChanged -> copy(search = message.text)
                }
            }
        ) {}

    private sealed interface Message {
        data class SearchChanged(val text: String) : Message
        data class GamesLoaded(
            val games: Flow<PagingData<GameEntity>>,
            val search: String,
            val isFilterEdited: Boolean
        ) : Message
    }

    private sealed interface Action {
        data object Init : Action
    }
}

private const val SEARCH_TIMEOUT_MILLIS = 500L