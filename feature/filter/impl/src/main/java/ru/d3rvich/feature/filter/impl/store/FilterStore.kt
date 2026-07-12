package ru.d3rvich.feature.filter.impl.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.entity.SortingEntity
import ru.d3rvich.core.model.FilterPreferencesBody
import ru.d3rvich.core.model.MetacriticRange
import ru.d3rvich.feature.filter.api.ListAction

internal interface FilterStore : Store<FilterStore.Intent, FilterStore.State, FilterStore.Label> {
    @Immutable
    data class State(
        val sortingList: ImmutableList<SortingEntity>,
        val platforms: ImmutableList<PlatformEntity>,
        val genres: ImmutableList<GenreFullEntity>,
        val filterPreferencesBody: FilterPreferencesBody,
    )

    sealed interface Intent {
        data object OnResetClicked : Intent
        data object OnApplyClicked : Intent
        data class OnSortChange(val sortBy: SortingEntity) : Intent
        data class OnReversedChange(val isReversed: Boolean) : Intent
        data class OnSelectedPlatformsChange(val action: ListAction<PlatformEntity>) : Intent
        data class OnSelectedGenresChange(val action: ListAction<GenreFullEntity>) : Intent
        data class OnMetacriticRangeChange(val range: MetacriticRange) : Intent
    }

    sealed interface Label {
        data object CloseScreen : Label
    }
}

