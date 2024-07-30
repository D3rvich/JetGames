package ru.d3rvich.filter.model

import ru.d3rvich.core.ui.base.UiEvent
import ru.d3rvich.core.domain.model.MetacriticRange
import ru.d3rvich.core.domain.entities.GenreFullEntity
import ru.d3rvich.core.domain.entities.PlatformEntity
import ru.d3rvich.core.domain.entities.SortingEntity

/**
 * Created by Ilya Deryabin at 29.02.2024
 */
internal sealed interface FilterUiEvent : UiEvent {
    data object OnResetClicked : FilterUiEvent
    data object OnApplyClicked : FilterUiEvent
    class OnSortChange(val sortBy: SortingEntity) : FilterUiEvent
    class OnReversedChange(val isReversed: Boolean) : FilterUiEvent
    class OnSelectedPlatformsChange(val action: ListAction<PlatformEntity>) : FilterUiEvent
    class OnSelectedGenresChange(val action: ListAction<GenreFullEntity>) : FilterUiEvent
    class OnMetacriticRangeChange(val range: MetacriticRange) : FilterUiEvent
}

internal sealed interface ListAction<out T : Any> {
    data class AddItem<T : Any>(val item: T) : ListAction<T>
    data class RemoveItem<T : Any>(val item: T) : ListAction<T>
    class Clear<T : Any> : ListAction<T>
}

internal fun <E : Any> MutableList<E>.update(action: ListAction<E>): MutableList<E> = this.apply {
    when (action) {
        is ListAction.AddItem -> add(action.item)
        is ListAction.Clear -> clear()
        is ListAction.RemoveItem -> remove(action.item)
    }
}