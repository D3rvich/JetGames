package ru.d3rvich.feature.home.impl.component

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.domain.preferences.FilterPreferences
import ru.d3rvich.core.domain.repositories.UserPreferencesRepository
import ru.d3rvich.core.domain.usecases.GetGamesUseCase
import ru.d3rvich.core.entity.GameEntity
import ru.d3rvich.core.model.ListDisplayOption
import ru.d3rvich.core.model.isDefault
import ru.d3rvich.core.ui.model.GameUiModel
import ru.d3rvich.core.ui.model.toGameUiModel
import ru.d3rvich.feature.home.api.HomeComponent
import kotlin.time.Duration.Companion.milliseconds

@Factory(binds = [DefaultHomeComponent::class])
internal class DefaultHomeComponent(
    @InjectedParam componentContext: ComponentContext,
    filterPreferences: FilterPreferences,
    private val getGamesUseCase: GetGamesUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    @InjectedParam private val output: (HomeComponent.Output) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val coroutineScope = coroutineScope()
    private val _searchFlow = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override val games: Flow<PagingData<GameUiModel>> = combine(
        _searchFlow.debounce { text -> if (text.isEmpty()) 0.milliseconds else 300.milliseconds },
        filterPreferences.filterPreferencesFlow, ::Pair
    ).flatMapLatest { (search, body) ->
        getGamesUseCase.invoke(search, body).map { pagingData ->
            pagingData.map(GameEntity::toGameUiModel)
        }
    }.flowOn(Dispatchers.Default).cachedIn(coroutineScope)

    override val model: StateFlow<HomeComponent.Model> = combine(
        _searchFlow,
        filterPreferences.filterPreferencesFlow.map { !it.isDefault() },
        userPreferencesRepository.getListDisplayOption()
    ) { search, isFilterEdited, listDisplayOption ->
        HomeComponent.Model(
            search = search,
            isFilterEdited = isFilterEdited,
            listDisplayOption = listDisplayOption
        )
    }.flowOn(Dispatchers.Default).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeComponent.Model(
            search = "",
            isFilterEdited = false,
            listDisplayOption = ListDisplayOption.Grid
        )
    )

    override fun openGameDetail(gameId: Int) {
        output(HomeComponent.Output.OpenGameDetail(gameId))
    }

    override fun openSettings() {
        output(HomeComponent.Output.OpenSettings)
    }

    override fun openFilter() {
        output(HomeComponent.Output.OpenFilter)
    }

    override fun setSearch(text: String) {
        _searchFlow.value = text
    }

    override fun setListVDisplayOption(listDisplayOption: ListDisplayOption) {
        coroutineScope.launch {
            userPreferencesRepository.setListDisplayOption(listDisplayOption)
        }
    }
}
