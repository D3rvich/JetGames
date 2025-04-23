package ru.d3rvich.home

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.preferences.isDefault
import ru.d3rvich.core.domain.usecases.GetGamesUseCase
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.ui.base.UiAction
import ru.d3rvich.home.model.HomeUiEvent
import ru.d3rvich.home.model.HomeUiState
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ilya Deryabin at 31.01.2024
 */
@OptIn(FlowPreview::class)
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getGamesUseCase: Provider<GetGamesUseCase>,
    filterPreferences: FilterPreferences,
) : BaseViewModel<HomeUiState, HomeUiEvent, UiAction>() {

    private val searchFlow = MutableStateFlow<String?>(null).also { flow ->
        flow.onEach { text ->
            if (text != null) {
                setState(currentState.copy(search = text))
            }
        }
            .debounce(SEARCH_TIMEOUT_MILLIS)
            .onEach { text ->
                if (text != null) {
                    loadGames(search = text)
                }
            }
            .launchIn(viewModelScope)
    }

    private val filterPreferencesFlow = filterPreferences.filterPreferencesFlow

    override fun createInitialState(): HomeUiState = HomeUiState(emptyFlow())

    init {
        loadGames()
        viewModelScope.launch {
            filterPreferencesFlow.collect { body ->
                loadGames(filerPrefBody = body)
            }
        }
    }

    override fun obtainEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearchChange -> {
                searchFlow.value = event.textSearch
            }

            HomeUiEvent.OnRefresh -> {
                loadGames()
            }
        }
    }

    private fun loadGames(
        search: String = searchFlow.value ?: "",
        filerPrefBody: FilterPreferencesBody = filterPreferencesFlow.value,
    ) {
        setState(
            HomeUiState(
                games = getGamesUseCase.get()
                    .invoke(search = search, filterPrefBody = filerPrefBody)
                    .cachedIn(viewModelScope),
                search = search,
                isFilterEdited = !filterPreferencesFlow.value.isDefault()
            )
        )
    }
}

private const val SEARCH_TIMEOUT_MILLIS = 500L