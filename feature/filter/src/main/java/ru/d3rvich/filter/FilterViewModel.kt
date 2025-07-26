package ru.d3rvich.filter

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.d3rvich.core.ui.base.BaseViewModel
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.preferences.FilterPreferencesBody
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import ru.d3rvich.core.domain.usecases.GetPlatformsUseCase
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.filter.model.FilterUiAction
import ru.d3rvich.filter.model.FilterUiEvent
import ru.d3rvich.filter.model.FilterUiState
import ru.d3rvich.filter.model.ListAction
import ru.d3rvich.filter.model.update
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
@HiltViewModel
internal class FilterViewModel @Inject constructor(
    private val filterPreferences: FilterPreferences,
    private val getPlatformsUseCase: Provider<GetPlatformsUseCase>,
    private val getGenresUseCase: Provider<GetGenresUseCase>,
) : BaseViewModel<FilterUiState, FilterUiEvent, FilterUiAction>() {

    init {
        setState(currentState.copy(filterPreferencesBody = filterPreferences.filterPreferencesFlow.value))
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getPlatformsUseCase.get().invoke().collect { status ->
                    if (status is LoadingResult.Success) {
                        setState(currentState.copy(platforms = status.value))
                    }
                }
            }
            launch {
                getGenresUseCase.get().invoke().collect { status ->
                    if (status is LoadingResult.Success) {
                        setState(currentState.copy(genres = status.value))
                    }
                }
            }
        }
    }

    override fun createInitialState(): FilterUiState = FilterUiState(
        sortingList = SortingEntity.entries,
        platforms = emptyList(),
        genres = emptyList(),
        filterPreferencesBody = FilterPreferencesBody.default()
    )

    override fun obtainEvent(event: FilterUiEvent) {
        when (event) {
            is FilterUiEvent.OnApplyClicked -> {
                filterPreferences.applyFilterPreferences(currentState.filterPreferencesBody)
                sendAction {
                    FilterUiAction.NavigateBack
                }
            }

            FilterUiEvent.OnResetClicked -> {
                val defaultBody = FilterPreferencesBody.default()
                updateFilterPref(defaultBody)
                filterPreferences.applyFilterPreferences(defaultBody)
                sendAction {
                    FilterUiAction.NavigateBack
                }
            }

            is FilterUiEvent.OnSortChange -> updateFilterPref(event.sortBy)

            is FilterUiEvent.OnReversedChange -> updateFilterPref(event.isReversed)

            is FilterUiEvent.OnSelectedGenresChange -> updateFilterPrefGenres(event.action)

            is FilterUiEvent.OnSelectedPlatformsChange -> updateFilterPrefPlatforms(event.action)

            is FilterUiEvent.OnMetacriticRangeChange -> updateFilterPref(event.range)
        }
    }

    private fun updateFilterPref(body: FilterPreferencesBody) {
        setState(currentState.copy(filterPreferencesBody = body))
    }

    private fun updateFilterPref(sortBy: SortingEntity) {
        val updatedFilterPrefBody = currentState.filterPreferencesBody.copy(sortBy = sortBy)
        setState(currentState.copy(filterPreferencesBody = updatedFilterPrefBody))
    }

    private fun updateFilterPref(isReversed: Boolean) {
        val updatedFilterPrefBody = currentState.filterPreferencesBody.copy(isReversed = isReversed)
        setState(currentState.copy(filterPreferencesBody = updatedFilterPrefBody))
    }

    private fun updateFilterPref(metacriticRange: MetacriticRange) {
        val updatedFilterPrefBody =
            currentState.filterPreferencesBody.copy(metacriticRange = metacriticRange)
        setState(currentState.copy(filterPreferencesBody = updatedFilterPrefBody))
    }

    private fun updateFilterPrefPlatforms(action: ListAction<PlatformEntity>) {
        val updatedList =
            currentState.filterPreferencesBody.selectedPlatforms.toMutableList().update(action)
        val updatedFilterPrefBody =
            currentState.filterPreferencesBody.copy(selectedPlatforms = updatedList.toList())
        setState(currentState.copy(filterPreferencesBody = updatedFilterPrefBody))
    }

    private fun updateFilterPrefGenres(action: ListAction<GenreFullEntity>) {
        val updatedList =
            currentState.filterPreferencesBody.selectedGenres.toMutableList().update(action)
        val updatedFilterPrefBody =
            currentState.filterPreferencesBody.copy(selectedGenres = updatedList.toList())
        setState(currentState.copy(filterPreferencesBody = updatedFilterPrefBody))
    }
}