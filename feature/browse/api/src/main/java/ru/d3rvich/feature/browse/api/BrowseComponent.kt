package ru.d3rvich.feature.browse.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import ru.d3rvich.core.entity.GenreFullEntity
import ru.d3rvich.core.entity.PlatformEntity
import ru.d3rvich.core.model.Result

@Stable
interface BrowseComponent {
    val genres: Value<Result<ImmutableList<GenreFullEntity>>>
    val platforms: Value<Result<ImmutableList<PlatformEntity>>>

    interface Factory {
        fun create(component: ComponentContext): BrowseComponent
    }
}