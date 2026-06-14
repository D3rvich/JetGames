package ru.d3rvich.feature.home.store

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import ru.d3rvich.core.domain.entities.GameEntity
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.preferences.isDefault
import ru.d3rvich.core.domain.usecases.GetGamesUseCase
import ru.d3rvich.feature.home.model.ListDisplayMode
import ru.d3rvich.feature.home.model.ListDisplayModeProvider
import kotlin.time.Duration.Companion.milliseconds

internal class HomeStoreFactory(
    private val storeFactory: StoreFactory,
    private val getGamesUseCase: GetGamesUseCase,
    private val filterPreferences: FilterPreferences,
    private val listDisplayModeProvider: ListDisplayModeProvider,
) {
    @OptIn(FlowPreview::class)
    fun create(): HomeStore = object : HomeStore,
        Store<HomeStore.Intent, HomeStore.State, Nothing> by storeFactory.create<HomeStore.Intent, Unit, Message, HomeStore.State, Nothing>(
            name = "HomeStore",
            initialState = HomeStore.State.Loading,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory {
                val searchFlow = MutableStateFlow("")
                val debounceSearchFlow = searchFlow.debounce(SEARCH_TIMEOUT_MILLIS.milliseconds)
                val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
                onAction<Unit> {
                    val gamesFlow = combine(
                        debounceSearchFlow,
                        filterPreferences.filterPreferencesFlow,
                        refreshTrigger.onStart { emit(Unit) }
                    ) { search, filterPreferencesBody, _ -> search to filterPreferencesBody }
                        .map { (search, body) ->
                            val games = getGamesUseCase.invoke(search, body).cachedIn(this)
                            Triple(games, search, body)
                        }.shareIn(
                            scope = this,
                            started = SharingStarted.WhileSubscribed(5000.milliseconds),
                            replay = 1
                        )
                    combine(gamesFlow, listDisplayModeProvider.listDisplayModeFlow.filterNotNull()) { triple, listDisplayMode ->
                        val (games, search, body) = triple
                        val isFilterEdited = !body.isDefault()
                        dispatch(
                            Message.Loaded(
                                games = games,
                                search = search,
                                isFilterEdited = isFilterEdited,
                                listDisplayMode = listDisplayMode
                            )
                        )
                    }.launchIn(this)
                }

                onIntent<HomeStore.Intent.SearchChange> { intent ->
                    searchFlow.value = intent.searchText
                    dispatch(Message.SearchChanged(intent.searchText))
                }
                onIntent<HomeStore.Intent.Refresh> {
                    refreshTrigger.tryEmit(Unit)
                }
                onIntent<HomeStore.Intent.ListDisplayChange> {
                    listDisplayModeProvider.setListViewMode(it.listDisplayMode)
                }
            },
            reducer = { message ->
                when (this) {
                    is HomeStore.State.Content -> {
                        when (message) {
                            is Message.Loaded -> copy(
                                games = message.games,
                                search = message.search,
                                isFilterEdited = message.isFilterEdited,
                                listDisplayMode = message.listDisplayMode
                            )

                            is Message.SearchChanged -> copy(search = message.text)
                        }
                    }

                    HomeStore.State.Loading -> {
                        if (message is Message.Loaded) {
                            HomeStore.State.Content(
                                games = message.games,
                                search = message.search,
                                isFilterEdited = message.isFilterEdited,
                                listDisplayMode = message.listDisplayMode
                            )
                        } else this
                    }
                }
            }
        ) {}

    private sealed interface Message {
        data class SearchChanged(val text: String) : Message

        data class Loaded(
            val games: Flow<PagingData<GameEntity>>,
            val search: String,
            val isFilterEdited: Boolean,
            val listDisplayMode: ListDisplayMode,
        ) : Message
    }
}

private const val SEARCH_TIMEOUT_MILLIS = 500L