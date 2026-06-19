package ru.d3rvich.feature.filter.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity
import ru.d3rvich.core.domain.model.LoadingResult
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.usecases.GetGenresUseCase
import ru.d3rvich.core.domain.usecases.GetPlatformsUseCase
import ru.d3rvich.feature.filter.model.FilterPreferencesBodyUiModel
import ru.d3rvich.feature.filter.model.toFilterPreferencesBody
import ru.d3rvich.feature.filter.model.toFilterPreferencesBodyUiModel
import ru.d3rvich.feature.filter.model.update

internal class FilterStoreFactory(
    private val storeFactory: StoreFactory,
    private val filterPreferences: FilterPreferences,
    private val getPlatformsUseCase: GetPlatformsUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) {
    fun create(): FilterStore = object : FilterStore,
        Store<FilterStore.Intent, FilterStore.State, FilterStore.Label> by storeFactory.create<FilterStore.Intent, Unit, Msg, FilterStore.State, FilterStore.Label>(
            name = "FilterStore",
            initialState = FilterStore.State(
                sortingList = SortingEntity.entries.toImmutableList(),
                platforms = emptyList<PlatformEntity>().toImmutableList(),
                genres = emptyList<GenreFullEntity>().toImmutableList(),
                filterPreferencesBody = filterPreferences.filterPreferencesFlow.value.toFilterPreferencesBodyUiModel()
            ),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory {
                onAction<Unit> {
                    filterPreferences.filterPreferencesFlow.map { body ->
                        Msg.UpdateFilterPreferencesBody(body.toFilterPreferencesBodyUiModel())
                    }
                        .flowOn(Dispatchers.Default)
                        .onEach { msg -> dispatch(msg) }
                        .launchIn(this)
                    combine(
                        getGenresUseCase.invoke(),
                        getPlatformsUseCase.invoke()
                    ) { genresResult, platformsResult ->
                        if (genresResult is LoadingResult.Success && platformsResult is LoadingResult.Success) {
                            Msg.SetGenresAndPlatforms(
                                genresResult.value.sortedBy(GenreFullEntity::name),
                                platformsResult.value.sortedBy(PlatformEntity::name)
                            )
                        } else null
                    }.filterNotNull()
                        .flowOn(Dispatchers.Default)
                        .onEach { msg -> dispatch(msg) }
                        .launchIn(this)
                }

                onIntent<FilterStore.Intent.OnResetClicked> {
                    filterPreferences.reset()
                    publish(FilterStore.Label.CloseScreen)
                }
                onIntent<FilterStore.Intent.OnApplyClicked> {
                    val currentBody = state().filterPreferencesBody.toFilterPreferencesBody()
                    filterPreferences.applyFilterPreferences(currentBody)
                    publish(FilterStore.Label.CloseScreen)
                }
                onIntent<FilterStore.Intent.OnSortChange> { intent ->
                    val updated = state().filterPreferencesBody.copy(sortBy = intent.sortBy)
                    dispatch(Msg.UpdateFilterPreferencesBody(updated))
                }
                onIntent<FilterStore.Intent.OnReversedChange> { intent ->
                    val updated = state().filterPreferencesBody.copy(isReversed = intent.isReversed)
                    dispatch(Msg.UpdateFilterPreferencesBody(updated))
                }
                onIntent<FilterStore.Intent.OnSelectedGenresChange> { intent ->
                    val updatedList = state().filterPreferencesBody.selectedGenres.toMutableSet()
                        .update(intent.action).toImmutableSet()
                    val updatedBody =
                        state().filterPreferencesBody.copy(selectedGenres = updatedList)
                    dispatch(Msg.UpdateFilterPreferencesBody(updatedBody))
                }
                onIntent<FilterStore.Intent.OnSelectedPlatformsChange> { intent ->
                    val currentBody = state().filterPreferencesBody
                    val updatedList =
                        currentBody.selectedPlatforms.toMutableSet().update(intent.action)
                            .toImmutableSet()
                    val updatedBody =
                        currentBody.copy(selectedPlatforms = updatedList)
                    dispatch(Msg.UpdateFilterPreferencesBody(updatedBody))
                }
                onIntent<FilterStore.Intent.OnMetacriticRangeChange> { intent ->
                    val updated = state().filterPreferencesBody.copy(metacriticRange = intent.range)
                    dispatch(Msg.UpdateFilterPreferencesBody(updated))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.SetGenresAndPlatforms -> copy(
                        genres = msg.genres.toImmutableList(),
                        platforms = msg.platforms.toImmutableList()
                    )

                    is Msg.UpdateFilterPreferencesBody -> copy(filterPreferencesBody = msg.body)
                }
            }
        ) {}

    private sealed interface Msg {
        data class SetGenresAndPlatforms(
            val genres: List<GenreFullEntity>,
            val platforms: List<PlatformEntity>
        ) : Msg

        data class UpdateFilterPreferencesBody(val body: FilterPreferencesBodyUiModel) : Msg
    }
}