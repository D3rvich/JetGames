package ru.d3rvich.feature.filter.impl.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.entity.SortingEntity
import ru.d3rvich.core.model.MetacriticRange
import ru.d3rvich.core.ui.utils.asValue
import ru.d3rvich.feature.filter.api.FilterComponent
import ru.d3rvich.feature.filter.api.ListAction
import ru.d3rvich.feature.filter.impl.store.FilterStore
import ru.d3rvich.feature.filter.impl.store.FilterStoreFactory

@Factory(binds = [DefaultFilterComponent::class])
internal class DefaultFilterComponent(
    @InjectedParam context: ComponentContext,
    storeFactory: FilterStoreFactory,
    @InjectedParam private val output: (FilterComponent.Output) -> Unit
) : FilterComponent, ComponentContext by context {
    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        doOnDestroy(scope::cancel)

        store.labels.onEach { label ->
            when (label) {
                FilterStore.Label.CloseScreen -> output(FilterComponent.Output.Finished)
            }
        }.launchIn(scope)
    }

    override val models: Value<FilterComponent.Model> = store.asValue().map(stateToModel)
    override fun close() {
        output(FilterComponent.Output.Finished)
    }

    override fun reset() {
        store.accept(FilterStore.Intent.OnResetClicked)
    }

    override fun apply() {
        store.accept(FilterStore.Intent.OnApplyClicked)
    }

    override fun setSorting(sortBy: SortingEntity) {
        store.accept(FilterStore.Intent.OnSortChange(sortBy))
    }

    override fun setIsReversed(isReversed: Boolean) {
        store.accept(FilterStore.Intent.OnReversedChange(isReversed))
    }

    override fun updateSelectedPlatforms(action: ListAction<PlatformEntity>) {
        store.accept(FilterStore.Intent.OnSelectedPlatformsChange(action))
    }

    override fun updateSelectedGenres(action: ListAction<GenreFullEntity>) {
        store.accept(FilterStore.Intent.OnSelectedGenresChange(action))
    }

    override fun setMetacriticRange(range: MetacriticRange) {
        store.accept(FilterStore.Intent.OnMetacriticRangeChange(range))
    }
}

internal val stateToModel: (FilterStore.State) -> FilterComponent.Model = { state ->
    with(state) {
        FilterComponent.Model(
            sortingList = sortingList,
            platforms = platforms,
            genres = genres,
            filterPreferencesBody = filterPreferencesBody
        )
    }
}