package ru.d3rvich.feature.filter.api

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.entity.SortingEntity
import ru.d3rvich.core.model.FilterPreferencesBody
import ru.d3rvich.core.model.MetacriticRange

@Stable
interface FilterComponent {
    val models: Value<Model>

    fun close()
    fun reset()
    fun apply()
    fun setSorting(sortBy: SortingEntity)
    fun setIsReversed(isReversed: Boolean)
    fun updateSelectedPlatforms(action: ListAction<PlatformEntity>)
    fun updateSelectedGenres(action: ListAction<GenreFullEntity>)
    fun setMetacriticRange(range: MetacriticRange)

    @Immutable
    data class Model(
        val sortingList: ImmutableList<SortingEntity>,
        val platforms: ImmutableList<PlatformEntity>,
        val genres: ImmutableList<GenreFullEntity>,
        val filterPreferencesBody: FilterPreferencesBody,
    )

    sealed interface Output {
        data object Finished : Output
    }

    interface Factory {
        fun create(context: ComponentContext, output: (Output) -> Unit): FilterComponent
    }
}
